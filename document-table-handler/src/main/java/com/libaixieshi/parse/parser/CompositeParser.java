package com.libaixieshi.parse.parser;

import com.libaixieshi.parse.domain.Table;
import com.libaixieshi.parse.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 复合解析器
 * @author chenl
 */
@Slf4j
public class CompositeParser extends AbstractTableParser<String> {

    private final List<AbstractTableParser<String>> parsers = new ArrayList<>();

    public CompositeParser(List<Class<? extends AbstractTableParser<String>>> parserClasses) {
        try {
            for (Class<? extends AbstractTableParser> parserClass : parserClasses) {
                parsers.add(parserClass.getDeclaredConstructor().newInstance());
            }
        } catch (Exception e) {
            log.error("不支持组合", e);
            throw new ServiceException("不支持组合" + e.getStackTrace().toString());
        }
    }

    @Override
    public List<? extends Table> parse(String s) {
        return parsers.stream()
                .map(parser -> parser.parse(s)).flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}