package com.mflyyou.cloud.common.env;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.Properties;

/**
 * 所有服务的默认配置,可以被 application.yml 或者 application-{profile}.yml 覆盖.
 *
 * 配置 spring.profiles.active:local,test
 * 配置加载优先级,从高到低.在优先级较高的配置文件找到属性,即返回
 * application-test.yml
 * application-local.yml
 * application.yml
 * application-default-test.yml
 * application-default-local.yml
 * application-default.yml
 */
@Slf4j
public class ApplicationDefaultEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    public static final int ORDER = ConfigDataEnvironmentPostProcessor.ORDER + 1;

    public static final String SEARCH_PROFILE_FORMAT = "classpath:application-default-%s.yml";

    private static final String DEFAULT_CONFIG = "classpath:application-default.yml";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        ResourceLoader resourceLoader = application.getResourceLoader();
        resourceLoader = (resourceLoader!=null) ? resourceLoader:new DefaultResourceLoader();

        // 当前 environment 配置的 propertySources
        MutablePropertySources propertySources = environment.getPropertySources();


        // 加载 application-default-{profile}.yml
        String[] activeProfiles = environment.getActiveProfiles();
        for (int i = activeProfiles.length - 1; i >= 0; i--) {
            String activeProfile = activeProfiles[i];
            String profileYmlName = String.format(SEARCH_PROFILE_FORMAT, activeProfile);
            Resource applicationProfilePathResource = resourceLoader.getResource(profileYmlName);
            if (applicationProfilePathResource.exists()) {
                log.info("--- loading default configurations: {}.", profileYmlName);
                propertySources.addLast(new PropertiesPropertySource(profileYmlName,
                        loadYamlIntoProperties(applicationProfilePathResource)));
            }
        }

        // 加载 application-default.yml
        Resource applicationDefaultPathResource = resourceLoader.getResource(DEFAULT_CONFIG);
        if (applicationDefaultPathResource.exists()) {
            log.info("--- loading default configurations: {}.", DEFAULT_CONFIG);
            propertySources.addLast(new PropertiesPropertySource(DEFAULT_CONFIG,
                    loadYamlIntoProperties(applicationDefaultPathResource)));
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    private Properties loadYamlIntoProperties(Resource resource) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource);
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}
