package com.mflyyou.cloud.common;

import com.mflyyou.cloud.common.config.WebMvcConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 因为 SpringBootApplication 标记的类,会自动扫描当前包及及子包的配置,为了避免包名不同加上此配置
 */

@Configuration
public class CloudCommonConfiguration {

    @Bean
    public WebMvcConfiguration cloudCommonWebMvcConfiguration() {
        return new WebMvcConfiguration();
    }
}
