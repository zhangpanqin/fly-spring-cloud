package com.mflyyou.cloud;

import com.mflyyou.cloud.order.OrderService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MockBeanConfiguration {
    @Bean
    @Primary
    public OrderService mockOrderService() {
        return Mockito.mock(OrderService.class);
    }
}