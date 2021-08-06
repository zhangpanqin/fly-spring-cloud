package com.mflyyou.cloud.env;

import org.springframework.boot.context.config.ConfigDataResource;
import org.springframework.boot.context.config.Profiles;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

public class MyExtConfigDataResource extends ConfigDataResource {

    private final boolean optional;

    private final Profiles profiles;

    private final ClassPathResource resource;

    protected MyExtConfigDataResource(boolean optional, Profiles profiles, ClassPathResource resource) {
        super(optional);
        this.optional = optional;
        this.profiles = profiles;
        this.resource = resource;
    }

    public ClassPathResource getResource() {
        return resource;
    }

    public Profiles getProfiles() {
        return profiles;
    }
}
