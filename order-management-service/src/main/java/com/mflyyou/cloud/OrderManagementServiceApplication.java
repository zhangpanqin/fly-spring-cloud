package com.mflyyou.cloud;

import com.mflyyou.cloud.order.AppConfig;
import com.mflyyou.cloud.order.ScopeTest;
import com.mflyyou.cloud.order.TestInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OrderManagementServiceApplication {
    @Autowired
    private AppConfig appConfig;

    @Value("${app.confi_test}")
    private String test2;

    public static void main(String[] args) {
        SpringApplication.run(OrderManagementServiceApplication.class, args);
    }

    //    @Scheduled(fixedDelay = 5000)
    public void run() {
        System.out.println(appConfig);
    }

    @Bean
    @Scope("prototype")
    public ScopeTest scopeTest() {
        return new ScopeTest();
    }

    @Bean
    public TestInit testInit() {
        return new TestInit();
    }
}
