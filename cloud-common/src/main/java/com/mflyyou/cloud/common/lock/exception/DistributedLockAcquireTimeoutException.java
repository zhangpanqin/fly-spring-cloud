package com.mflyyou.cloud.common.lock.exception;


import com.mflyyou.cloud.common.lock.DistributedLock.LockType;

/**
 * 超时没有获取到锁
 */
public class DistributedLockAcquireTimeoutException extends AbstractDistributedLockAcquireException {

    public DistributedLockAcquireTimeoutException(String lockName, LockType lockType) {
        super(String.format("Acquire lock timeout, lock name is [%s] and type is [%s]", lockName, lockType.name()));
    }

}
