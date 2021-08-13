package com.mflyyou.cloud.common.lock.exception;


import com.mflyyou.cloud.common.lock.DistributedLock.LockType;

/**
 * 获取锁的时候抛出了异常
 */
public class DistributedLockAcquireException extends AbstractDistributedLockAcquireException {

    public DistributedLockAcquireException(String lockName, LockType lockType, Throwable e) {
        super(String.format("Can not acquire DistributedLock, lock name is [%s] and type is [%s]", lockName, lockType.name()), e);
    }
}
