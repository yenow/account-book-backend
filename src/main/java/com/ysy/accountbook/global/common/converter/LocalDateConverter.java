package com.ysy.accountbook.global.common.converter;

import com.ysy.accountbook.domain.trade.dto.TradeSaveResponse;
import com.ysy.accountbook.domain.trade.entity.Trade;
import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter implements Converter<LocalDate, String> {
    @Override
    public String convert(LocalDate source) {
        return source.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
