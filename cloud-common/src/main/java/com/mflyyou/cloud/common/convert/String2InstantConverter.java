package com.mflyyou.cloud.common.convert;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.time.Instant;

@Slf4j
public class String2InstantConverter implements Converter<String, Instant> {

    @Override
    public Instant convert(@Nullable String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        try {
            return Instant.ofEpochMilli(Long.parseLong(source));
        } catch (NumberFormatException e) {
            return Instant.parse(source);
        }
    }
}