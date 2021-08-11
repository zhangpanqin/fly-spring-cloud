package com.mflyyou.cloud.common.web.convert;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mflyyou.cloud.common.constant.TimeFormatterConstant.DATETIME_FORMAT;

@Slf4j
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {


    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

    @Override
    public LocalDateTime convert(@Nullable String source) {
        return LocalDateTime.parse(source, dateTimeFormatter);
    }
}