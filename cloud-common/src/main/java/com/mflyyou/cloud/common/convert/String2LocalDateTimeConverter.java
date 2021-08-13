package com.mflyyou.cloud.common.convert;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

import static com.mflyyou.cloud.common.constant.TimeFormatterConstant.DATE_TIME_FORMATTER;

@Slf4j
public class String2LocalDateTimeConverter implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(@Nullable String source) {
        return LocalDateTime.parse(source, DATE_TIME_FORMATTER);
    }
}