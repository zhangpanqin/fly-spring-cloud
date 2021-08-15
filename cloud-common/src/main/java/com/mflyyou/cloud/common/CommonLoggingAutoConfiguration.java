package com.mflyyou.cloud.common;

import com.mflyyou.cloud.common.log.LoggingAdvisor;
import org.aopalliance.aop.Advice;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import static com.mflyyou.cloud.common.constant.AopOrderConstant.LOGGING_AOP_CONSTANT;

/**
 * 打印日志
 */

@ConditionalOnProperty(name = "app.logging.enable", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass({Advice.class})
public class CommonLoggingAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    static class LoggingAdvisorConfiguration {
        @Bean
        @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
        public LoggingAdvisor buildCommonLoggingAdvisor() {
            LoggingAdvisor loggingAdvisor = new LoggingAdvisor();
            loggingAdvisor.setOrder(LOGGING_AOP_CONSTANT);
            return loggingAdvisor;
        }
    }
}
