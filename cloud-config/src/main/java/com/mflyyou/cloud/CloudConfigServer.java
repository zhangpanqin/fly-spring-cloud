package com.mflyyou.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableConfigServer
@SpringBootApplication
@EnableScheduling
public class CloudConfigServer {

    @Autowired
    private ConfigurableApplicationContext context;

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(CloudConfigServer.class, args);
        System.out.println(run.getEnvironment().getPropertySources());
    }

    @Scheduled(fixedDelay = 5000)
    public void run(){
        System.out.println(context.getEnvironment().getPropertySources());
    }

}
