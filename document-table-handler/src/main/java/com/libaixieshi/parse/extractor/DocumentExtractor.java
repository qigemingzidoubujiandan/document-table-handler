package com.libaixieshi.parse.extractor;

import com.libaixieshi.parse.extractor.result.ExtractedResult;

/**
 * 处理
 * @author chenl
 */
public interface DocumentExtractor<T, R extends ExtractedResult> {

    /**
     * 抽取
     */
    R extract(T t);
}
