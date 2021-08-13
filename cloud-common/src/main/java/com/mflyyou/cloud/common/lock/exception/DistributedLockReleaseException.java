package com.mflyyou.cloud.common.lock.exception;

import com.mflyyou.cloud.common.lock.DistributedLock.LockType;


public class DistributedLockReleaseException extends AbstractDistributedLockAcquireException {

    public DistributedLockReleaseException(String lockName, LockType lockType, Throwable e) {
        super(String.format("Lock name is [%s] and type is [%s], failure to unlock", lockName, lockType.name()), e);
    }
}
