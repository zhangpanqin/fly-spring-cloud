package com.mflyyou.cloud.common;

import com.mflyyou.cloud.common.lock.DistributedLockAdvisor;
import com.mflyyou.cloud.common.lock.executor.DefaultDistributedLockExecutor;
import com.mflyyou.cloud.common.lock.executor.DistributedLockExecutor;
import org.aopalliance.aop.Advice;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import javax.validation.constraints.NotNull;

@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnProperty(name = "app.distributed_lock.enable", havingValue = "true", matchIfMissing = true)
@ConditionalOnBean(RedissonClient.class)
@ConditionalOnClass({Advice.class, RedissonClient.class})
public class DistributedLockAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    static class DistributedLockAdvisorConfiguration {

        @Bean
        @Order(Ordered.LOWEST_PRECEDENCE + 1000)
        public DistributedLockAdvisor distributedLockAdvisor(DistributedLockExecutor distributedLockExecutor) {
            return new DistributedLockAdvisor(distributedLockExecutor);
        }

        @Bean
        @ConditionalOnMissingBean(DistributedLockExecutor.class)
        public DistributedLockExecutor buildDefaultDistributedLockExecutor(@NotNull RedissonClient redissonClient,
                                                                           Environment environment) {
            return new DefaultDistributedLockExecutor(redissonClient, environment);
        }
    }

}