package com.mflyyou.cloud.common.config;

import com.mflyyou.cloud.common.web.convert.StringToInstantConverter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        System.out.println("执行了");
        registry.addConverter(new StringToInstantConverter());
    }
}
