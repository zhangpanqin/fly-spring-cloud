package com.mflyyou.cloud.common;

import com.mflyyou.cloud.common.config.WebMvcConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Servlet;

/**
 * 因为 SpringBootApplication 标记的类,会自动扫描当前包及及子包的配置,为了避免包名不同加上此配置
 * <p>
 * jackson 日期反序列化和序列化
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
public class CommonWebMvcAutoConfiguration {

    @Bean
    public WebMvcConfiguration cloudCommonWebMvcConfiguration() {
        return new WebMvcConfiguration();
    }

}
