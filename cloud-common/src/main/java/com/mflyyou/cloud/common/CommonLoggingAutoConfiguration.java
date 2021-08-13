package com.mflyyou.cloud.common;

import com.mflyyou.cloud.common.log.LoggingAdvisor;
import org.aopalliance.aop.Advice;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 打印日志
 */

@ConditionalOnProperty(name = "app.logging.enable", havingValue = "true", matchIfMissing = true)
public class CommonLoggingAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({Advice.class})
    static class LoggingAdvisorConfiguration {
        @Bean
        @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
        @Order(Ordered.HIGHEST_PRECEDENCE - 1000)
        public LoggingAdvisor buildCommonLoggingAdvisor() {
            return new LoggingAdvisor();
        }
    }
}
