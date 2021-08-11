package com.mflyyou.cloud.common.web.convert;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.mflyyou.cloud.common.constant.TimeFormatterConstant.DATE_FORMAT;

@Slf4j
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    @Override
    public LocalDate convert(@Nullable String source) {
        return LocalDate.parse(source, dateTimeFormatter);
    }
}