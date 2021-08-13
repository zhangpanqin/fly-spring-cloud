package com.mflyyou.cloud.common.convert;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

import static com.mflyyou.cloud.common.constant.TimeFormatterConstant.DATE_FORMATTER;

@Slf4j
public class String2LocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(@Nullable String source) {
        return LocalDate.parse(source, DATE_FORMATTER);
    }
}