package com.mflyyou.cloud.common.config;


import com.mflyyou.cloud.common.convert.LocalDate2StringConverter;
import com.mflyyou.cloud.common.convert.String2InstantConverter;
import com.mflyyou.cloud.common.convert.String2LocalDateConverter;
import com.mflyyou.cloud.common.convert.String2LocalDateTimeConverter;
import com.mflyyou.cloud.common.exception.resolver.CommonHandlerExceptionResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

public class WebMvcConfiguration implements WebMvcConfigurer {


    @Override
    public void addFormatters(FormatterRegistry registry) {
        // form 表单个 query String 用下面的 convert 转换参数
        registry.addConverter(new String2InstantConverter());
        registry.addConverter(new String2LocalDateConverter());
        registry.addConverter(new String2LocalDateTimeConverter());
        registry.addConverter(new LocalDate2StringConverter());
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        // 配置在 HandlerExceptionResolverComposite 中第一个异常处理,不允许 @ExceptionHandler 重写异常信息,只能通过注册 HandlerExceptionResolver bean 来处理
        resolvers.add(0, new CommonHandlerExceptionResolver());
        // 配置在 HandlerExceptionResolverComposite 中第二个异常处理,便于项目通过 @ExceptionHandler 重写异常信息
//        resolvers.add(1, new CommonHandlerExceptionResolver());
    }
}
