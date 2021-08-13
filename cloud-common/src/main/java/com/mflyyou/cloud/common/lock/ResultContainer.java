package com.mflyyou.cloud.common.lock;


import com.mflyyou.cloud.common.lock.exception.DistributedLockTaskException;

import java.util.concurrent.Callable;

public class ResultContainer<T> implements Runnable {
    private final Callable<T> callable;
    private Object result;

    public ResultContainer(Callable<T> callable) {
        this.callable = callable;
    }

    @Override
    public void run() {
        try {
            result = callable.call();
        } catch (Exception e) {
            throw new DistributedLockTaskException(e);
        }
    }

    public T getResult() {
        return (T) result;
    }
}
