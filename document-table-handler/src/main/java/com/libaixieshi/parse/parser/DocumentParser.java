package com.libaixieshi.parse.parser;

/**
 * @author chenl
 */
public interface DocumentParser<T, R> {

    /**
     * 解析
     */
    R parse(T t);
}
