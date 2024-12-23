package com.libaixieshi.parse;

import cn.hutool.core.thread.ThreadUtil;
import com.libaixieshi.parse.exception.ServiceException;
import com.libaixieshi.parse.extractor.ExtractorConfig;
import com.libaixieshi.parse.extractor.ExtractorFactory;
import com.libaixieshi.parse.extractor.DocumentExtractor;
import com.libaixieshi.parse.extractor.result.ExtractedResult;
import com.libaixieshi.parse.extractor.result.TableExtractedResult;
import com.libaixieshi.parse.filefetcher.FileFetcher;
import com.libaixieshi.parse.parser.DocumentParser;
import com.libaixieshi.parse.parser.DocumentParserFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chenl
 */
@Slf4j
public class DocumentProcessor {

    private final ExecutorService executorService;

    public DocumentProcessor(int threadPoolSize) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }
    

    /**
     * 处理文件列表并提取信息。
     *
     * @param fileFetcher 文件获取器
     * @param config      提取配置
     * @return 提取结果列表
     */
    public ExtractedResult processFiles(FileFetcher fileFetcher, ExtractorConfig config) {
        try {
            // 0. 获取文件路径列表
            String sourcePath = fileFetcher.fetchFile();

            // 1. 根据配置获取合适的解析器和抽取器
            DocumentExtractor<Object, ? extends ExtractedResult> extractor = ExtractorFactory.createExtractor(config);

            // 2. 创建解析器（这里假设所有文件类型相同，否则需要根据文件类型分别创建）
            DocumentParser<String, ?> parser = DocumentParserFactory.createParserByFilePath(sourcePath, config.getParseType());

            // 3. 使用 CompletableFuture 并发处理文件
            CompletableFuture<? extends ExtractedResult> future = CompletableFuture.supplyAsync(
                    () -> processSingleFile(sourcePath, parser, extractor),
                    executorService
            ).exceptionally(ex -> {
                log.error("【DocumentProcessor】 Failed to process file at path: {}", sourcePath, ex);
                return null; // 返回空的结果以继续处理其他文件
            });

            // 4. 等待所有 CompletableFuture 完成并收集结果
            ExtractedResult result = future.get();

            log.info("【DocumentProcessor】 Finished processing all files.");
//            List<ExtractedResult> results = sourcePath.stream().map(path -> processSingleFile(path, parser, extractor)).collect(Collectors.toList());
            fillEmptyCellExtractedResult(result);
            return result;

        } catch (Exception e) {
            log.error("【DocumentProcessor】 Failed to process files", e);
            throw new ServiceException("【DocumentProcessor】 Failed to process files");
        } finally {
            // 5. 关闭线程池
            if (!executorService.isShutdown()) {
                executorService.shutdown();
            }
        }
    }

    private void fillEmptyCellExtractedResult(ExtractedResult extractedResult) {
        if (!(extractedResult instanceof TableExtractedResult)) {
            return;
        }

        TableExtractedResult tableResult = (TableExtractedResult) extractedResult;
        List<List<String>> tableData = tableResult.getTableData();

        if (tableData == null || tableData.isEmpty()) {
            return;
        }

        // 计算最大列数
        int maxColumns = tableData.stream()
                .filter(Objects::nonNull)
                .mapToInt(List::size)
                .max()
                .orElse(0);

        // 填充每一行，使其具有相同的列数
        for (List<String> row : tableData) {
            if (row != null && row.size() < maxColumns) {
                row.addAll(Collections.nCopies(maxColumns - row.size(), "-"));
            }
        }
    }

    /**
     * 处理单个文件并提取信息。
     *
     * @param sourcePath 文件路径
     * @param parser     解析器
     * @param extractor  抽取器
     * @return 提取结果
     */
    private ExtractedResult processSingleFile(String sourcePath, DocumentParser<String, ?> parser, DocumentExtractor<Object, ? extends ExtractedResult> extractor) {
        try {
            log.debug("【DocumentProcessor】 Processing file at path: {}", sourcePath);

            // 解析文件
            Object parsed = parser.parse(sourcePath);
            log.debug("【DocumentProcessor】 Parsed content from file: {}", sourcePath);

            // 抽取信息
            ExtractedResult result = extractor.extract(parsed);
            log.debug("【DocumentProcessor】 Extracted result for file: {}", sourcePath);
            ThreadUtil.sleep(500);
            return result;

        } catch (Exception e) {
            log.error("【DocumentProcessor】 Failed to process file at path: {}", sourcePath, e);
            return null; // 返回 null 以继续处理其他文件
        }
    }

}