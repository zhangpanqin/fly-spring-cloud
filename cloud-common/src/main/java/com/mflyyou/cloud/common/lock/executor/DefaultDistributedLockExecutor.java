package com.mflyyou.cloud.common.lock.executor;

import com.mflyyou.cloud.common.lock.DistributedLock.LockType;
import com.mflyyou.cloud.common.lock.exception.DistributedLockAcquireException;
import com.mflyyou.cloud.common.lock.exception.DistributedLockAcquireTimeoutException;
import com.mflyyou.cloud.common.lock.exception.DistributedLockReleaseException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.WriteRedisConnectionException;
import org.springframework.core.env.Environment;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.mflyyou.cloud.common.lock.DistributedLock.LeaseTime.INFINITE;
import static com.mflyyou.cloud.common.lock.DistributedLock.WaitTime.NO_WAIT;

@Slf4j
public class DefaultDistributedLockExecutor extends AbstractDistributedLockExecutor {

    private static final long WAIT_TIME_MIN = 5L;

    public DefaultDistributedLockExecutor(RedissonClient redissonClient, Environment environment) {
        super(redissonClient, environment);
    }

    /**
     * @param key              锁的唯一标识
     * @param addAppNameAndEnv 锁标识是否添加 spring.application.name 和 active profiles 作为锁标识的一部分
     * @param waitTime         定义获取锁的超时时间,超时没有获取到,抛出异常 {@link DistributedLockAcquireTimeoutException}
     * @param leaseTime        获取到锁之后,定义锁的持有时间,超过这个时间,自动释放掉.不想锁自动释放掉,设置小于等于 0
     * @param lockType         定义锁的类型,公平锁(),非公平锁,读写锁()
     */

    @Override
    public void execute(Runnable runnable, String key, LockType lockType, long waitTime, long leaseTime, boolean addAppNameAndEnv) {
        Objects.requireNonNull(runnable);
        Objects.requireNonNull(lockType);
        RLock lock = getLockFromLockType(key, lockType, addAppNameAndEnv);
        if (waitTime <= NO_WAIT) {
            waitTime = WAIT_TIME_MIN;
        }
        if (leaseTime <= INFINITE) {
            leaseTime = -1;
        }
        try {
            // 因为需要网络通信 redis,设置锁的状态,如果此时 redis 实例挂了,会抛出异常
            if (lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS)) {
                // 获取到锁,执行业务
                try {
                    runnable.run();
                } finally {
                    try {
                        lock.unlock();
                    } catch (Exception e) {
                        log.error("DistributedLock unlock error", e);
                        throw new DistributedLockReleaseException(key, lockType, e);
                    }
                }
            } else {
                // 超时没有获取到分布式锁,抛出异常,让调用层处理
                throw new DistributedLockAcquireTimeoutException(key, lockType);
            }
        } catch (InterruptedException e) {
            log.error("be interrupted when keeping or getting DistributedLock", e);
            // 获取锁的时候,抛出了异常(可能被别的线程打断了当前线程)
            throw new DistributedLockAcquireException(key, lockType, e);
        } catch (WriteRedisConnectionException e) {
            // 没有实例可以设置锁的标识
            log.error("no redis can write for getting DistributedLock", e);
            throw new DistributedLockAcquireException(key, lockType, e);
        }
    }

    @Override
    public <T> T execute(Callable<T> runnable, String key, LockType lockType, long waitTime, long leaseTime, boolean addAppNameAndEnv) {
        ResultContainer<T> tResultContainer = new ResultContainer<>(runnable);
        execute(tResultContainer, key, lockType, waitTime, leaseTime, addAppNameAndEnv);
        return tResultContainer.getResult();
    }

}