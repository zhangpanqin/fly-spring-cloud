package com.mflyyou.cloud.common.lock.exception;

/**
 * 分布式锁执行的业务代码报错
 */
public class DistributedLockTaskException extends AbstractDistributedLockAcquireException {
    public DistributedLockTaskException(String message, Throwable e) {
        super(message, e);
    }

    public DistributedLockTaskException(Throwable e) {
        super(e);
    }
}
