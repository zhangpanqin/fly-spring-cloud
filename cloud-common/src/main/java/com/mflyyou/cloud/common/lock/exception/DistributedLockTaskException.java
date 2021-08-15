package com.mflyyou.cloud.common.lock.exception;

/**
 * 分布式锁执行的业务代码报错
 */
public class DistributedLockTaskException extends AbstractDistributedLockAcquireException {
    private final Throwable original;

    public DistributedLockTaskException(Throwable e) {
        super(e.getMessage(), e);
        this.original = e;
    }

    public Throwable getOriginal() {
        return original;
    }
}
