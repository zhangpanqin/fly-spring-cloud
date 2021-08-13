package com.mflyyou.cloud.common.lock.exception;


/**
 * 执行业务的时候抛出了异常
 */
public abstract class AbstractDistributedLockAcquireException extends RuntimeException {

    public AbstractDistributedLockAcquireException(String message, Throwable e) {
        super(message, e);
    }

    public AbstractDistributedLockAcquireException(String message) {
        super(message);
    }

    public AbstractDistributedLockAcquireException(Throwable e) {
        super(e);
    }
}
