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
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.validation.constraints.NotNull;

import static com.mflyyou.cloud.common.constant.AopOrderConstant.DISTRIBUTED_LOCK_AOP_CONSTANT;
import static com.mflyyou.cloud.common.constant.AopOrderConstant.TRANSACTION_AOP_CONSTANT;

@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnProperty(name = "app.distributed_lock.enable", havingValue = "true", matchIfMissing = true)
@ConditionalOnBean(RedissonClient.class)
@ConditionalOnClass({Advice.class, RedissonClient.class})
public class DistributedLockAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @EnableTransactionManagement(order= TRANSACTION_AOP_CONSTANT)
    static class DistributedLockAdvisorConfiguration {

        @Bean
        public DistributedLockAdvisor distributedLockAdvisor(DistributedLockExecutor distributedLockExecutor) {
            DistributedLockAdvisor distributedLockAdvisor = new DistributedLockAdvisor(distributedLockExecutor);
            distributedLockAdvisor.setOrder(Ordered.LOWEST_PRECEDENCE);
            return distributedLockAdvisor;
        }

        @Bean
        @ConditionalOnMissingBean(DistributedLockExecutor.class)
        public DistributedLockExecutor buildDefaultDistributedLockExecutor(@NotNull RedissonClient redissonClient,
                                                                           Environment environment) {
            return new DefaultDistributedLockExecutor(redissonClient, environment);
        }
    }

}