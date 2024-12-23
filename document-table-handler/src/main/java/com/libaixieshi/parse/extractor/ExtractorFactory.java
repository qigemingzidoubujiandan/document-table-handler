package com.libaixieshi.parse.extractor;

import com.libaixieshi.parse.domain.Enum.ParseTypeEnum;
import com.libaixieshi.parse.domain.TableTypeEnum;
import com.libaixieshi.parse.extractor.result.ExtractedResult;

/**
 * @author chenl
 */
public class ExtractorFactory {

    @SuppressWarnings("unchecked")
    public static <T, R extends ExtractedResult> DocumentExtractor<T, R> createExtractor(ExtractorConfig extractorConfig) {
        ParseTypeEnum parseType = extractorConfig.getParseType();
        if (parseType.equals(ParseTypeEnum.TABLE)) {
            switch (TableTypeEnum.fromCode(extractorConfig.getTableType().getCode())) {
                case KV:
                    return (DocumentExtractor<T, R>) new MapDocExtractor(extractorConfig);
                case LIST:
                    return (DocumentExtractor<T, R>) new ListDocExtractor(extractorConfig);
                default:
                    throw new IllegalArgumentException("Unsupported table type");
            }
        } else if (parseType.equals(ParseTypeEnum.TEXT)) {
            return (DocumentExtractor<T, R>) new BaseTextExtractor(extractorConfig);
        } else {
            throw new IllegalArgumentException("Unsupported parse type");
        }
    }
}