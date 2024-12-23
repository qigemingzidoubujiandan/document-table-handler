package com.libaixieshi.parse.parser;

public abstract class AbstractTextParser<T> implements DocumentParser<T, String> {

    /**
     * 解析
     */
    @Override
    public abstract String parse(T t);
}
