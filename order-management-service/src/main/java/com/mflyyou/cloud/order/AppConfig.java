package com.mflyyou.cloud.order;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private String confi_test = "默认值";

    @PostConstruct
    public void postCon() {
        System.out.println("PostConstruct");

    }

    public void test() {
        System.out.println(this);
    }

    @PreDestroy
    public void destroy() {
        System.out.println("PreDestroy");
    }
}
