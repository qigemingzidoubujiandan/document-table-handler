package com.libaixieshi.parse;

import cn.hutool.json.JSONUtil;
import com.libaixieshi.parse.domain.Enum.ParseTypeEnum;
import com.libaixieshi.parse.domain.TableTypeEnum;
import com.libaixieshi.parse.extractor.ExtractorConfig;
import com.libaixieshi.parse.extractor.result.ExtractedResult;
import org.junit.Test;

public class DocumentProcessorTest {

   @Test
   public void test1() {
       DocumentProcessor documentProcessor = new DocumentProcessor(1);
       ExtractorConfig extractorConfig = new ExtractorConfig.Builder()
               .setParseType(ParseTypeEnum.TABLE)
               .setTableType(TableTypeEnum.LIST)
               .setConditions(new String[]{"序号","资产名称","资产余额","占产品净资产的比例"})
               .setExpectParseRowSize(11)
               .setIsSmartHandle(Boolean.TRUE)
               .build();
       ExtractedResult extractedResult = documentProcessor.processFiles(
               () -> "D:\\pdf\\恒丰银行股份有限公司_理财产品2023年上半年度报告\\恒丰银行恒心季季盈第2期_2023年上半年度报告.docx",
               extractorConfig);
       System.out.println(JSONUtil.toJsonStr(extractedResult));
   }

}