package com.mflyyou.cloud;


import com.mflyyou.cloud.user.OrderClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class MockBeanConfiguration {

    @Bean
    @Primary
    public OrderClient orderClient() {
        return Mockito.mock(OrderClient.class);
    }
}
