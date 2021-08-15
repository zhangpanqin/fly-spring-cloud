package com.mflyyou.cloud.common.lock.exception;


/**
 * 执行业务的时候抛出了异常
 */
public abstract class AbstractDistributedLockAcquireException extends RuntimeException {

    protected AbstractDistributedLockAcquireException(String message, Throwable e) {
        super(message, e);
    }

    protected AbstractDistributedLockAcquireException(String message) {
        super(message);
    }

    protected AbstractDistributedLockAcquireException(Throwable e) {
        super(e);
    }
}
