package com.mflyyou.cloud;

import com.mflyyou.cloud.user.UserService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@ActiveProfiles("test")
public class MockBeanConfiguration {
    @Bean
    @Primary
    public UserService mockOrderService() {
        return Mockito.mock(UserService.class);
    }
}