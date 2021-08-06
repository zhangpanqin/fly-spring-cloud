package com.mflyyou.cloud.env;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.config.ConfigData;
import org.springframework.boot.context.config.ConfigDataLoader;
import org.springframework.boot.context.config.ConfigDataLoaderContext;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.boot.context.config.Profiles;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class MyConfigDataLoader implements ConfigDataLoader<MyExtConfigDataResource>, Ordered {


    @Override
    public ConfigData load(ConfigDataLoaderContext context, MyExtConfigDataResource resource) throws IOException, ConfigDataResourceNotFoundException {

        ClassPathResource classPathResource = resource.getResource();
        String name = String.format("Config resource '%s' via location '%s'", "classpath", resource.getResource());
        Properties properties = loadYamlIntoProperties(classPathResource);
        return new ConfigData(List.of(new OriginTrackedMapPropertySource(name, properties)), (propertySource) -> {
            String propertySourceName = propertySource.getName();
            List<ConfigData.Option> options = new ArrayList<>();
            options.add(ConfigData.Option.IGNORE_IMPORTS);
            options.add(ConfigData.Option.IGNORE_PROFILES);
            for (String profile : Optional.ofNullable(resource.getProfiles()).map(Profiles::getAccepted).orElseGet(List::of)) {
                if (propertySourceName.contains("-" + profile + ".")) {
                    options.add(ConfigData.Option.PROFILE_SPECIFIC);
                }
            }
            return ConfigData.Options.of(options.toArray(new ConfigData.Option[0]));
        });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1110;
    }

    private Properties loadYamlIntoProperties(Resource resource) {
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(resource);
        yamlPropertiesFactoryBean.afterPropertiesSet();
        return yamlPropertiesFactoryBean.getObject();
    }
}