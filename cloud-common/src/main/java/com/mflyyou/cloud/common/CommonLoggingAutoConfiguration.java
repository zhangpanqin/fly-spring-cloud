package com.mflyyou.cloud.common;

import com.mflyyou.cloud.common.log.LoggingAdvisor;
import org.aspectj.lang.reflect.Advice;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

/**
 * 打印日志
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({Advice.class})
@ConditionalOnProperty(name = "app.logging.enable", havingValue = "true", matchIfMissing = true)
public class CommonLoggingAutoConfiguration {

    @Bean
    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    public LoggingAdvisor buildCommonLoggingAdvisor() {
        return new LoggingAdvisor();
    }
}
