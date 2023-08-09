package com.ysy.accountbook.global.common.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter implements Converter<LocalDate, String> {
    @Override
    public String convert(LocalDate source) {
        return source.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
