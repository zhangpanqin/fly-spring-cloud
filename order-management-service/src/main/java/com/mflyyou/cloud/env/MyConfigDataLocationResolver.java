package com.mflyyou.cloud.env;

import org.springframework.boot.context.config.ConfigDataLocation;
import org.springframework.boot.context.config.ConfigDataLocationNotFoundException;
import org.springframework.boot.context.config.ConfigDataLocationResolver;
import org.springframework.boot.context.config.ConfigDataLocationResolverContext;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.boot.context.config.Profiles;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.List;

public class MyConfigDataLocationResolver
        implements ConfigDataLocationResolver<MyExtConfigDataResource>, Ordered {
    public static final String PREFIX = "my:";

    @Override
    public boolean isResolvable(ConfigDataLocationResolverContext context, ConfigDataLocation location) {
        if (location.hasPrefix(getPrefix())) {
            return true;
        }
        return false;
    }

    @Override
    public List<MyExtConfigDataResource> resolve(ConfigDataLocationResolverContext context, ConfigDataLocation location) throws ConfigDataLocationNotFoundException, ConfigDataResourceNotFoundException {
        String uris = location.getNonPrefixedValue(getPrefix());
        ClassPathResource classPathResource = new ClassPathResource(uris + "/application.yml");
        MyExtConfigDataResource myExtConfigDataResource = new MyExtConfigDataResource(true, null, classPathResource);
        return List.of(myExtConfigDataResource);
    }

    @Override
    public List<MyExtConfigDataResource> resolveProfileSpecific(
            ConfigDataLocationResolverContext resolverContext,
            ConfigDataLocation location,
            Profiles profiles)
            throws ConfigDataLocationNotFoundException {
        String uris = location.getNonPrefixedValue(getPrefix());
        List<MyExtConfigDataResource> ret = new ArrayList<>();
        for (String active : profiles.getActive()) {
            ClassPathResource classPathResource = new ClassPathResource(String.format(uris + "/application-%s.yml", active));
            MyExtConfigDataResource myExtConfigDataResource = new MyExtConfigDataResource(true, profiles, classPathResource);
            ret.add(myExtConfigDataResource);
        }
        return ret;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE+1110;
    }

    protected String getPrefix() {
        return PREFIX;
    }
}