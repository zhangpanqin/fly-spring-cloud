package com.mflyyou.cloud.common.lock.executor;

import com.mflyyou.cloud.common.lock.DistributedLock.LockType;

import java.util.concurrent.Callable;

public interface DistributedLockExecutor {
    /**
     * @param runnable  需要保护的竞争资源
     * @param key       锁的唯一标识
     * @param lockType  锁的类型
     * @param waitTime  获取锁等待的超时时间
     * @param leaseTime 持有锁的时间,-1,标识永久持有(客户端),直到解锁
     */
    void execute(Runnable runnable, String key, LockType lockType, long waitTime, long leaseTime, boolean addAppNameAndEnv) throws InterruptedException;

    <T> T execute(LockTask<T> runnable, String key, LockType lockType, long waitTime, long leaseTime, boolean addAppNameAndEnv);
}