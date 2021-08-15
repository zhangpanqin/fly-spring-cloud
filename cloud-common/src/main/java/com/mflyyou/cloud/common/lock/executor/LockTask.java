package com.mflyyou.cloud.common.lock.executor;


@FunctionalInterface
public interface LockTask<T> {
    T run();

    class LockTaskForRunnable<T> implements Runnable {

        private final LockTask<T> lockTask;

        private T result;

        public LockTaskForRunnable(LockTask<T> lockTask) {
            this.lockTask = lockTask;
        }

        @Override
        public void run() {
            result = lockTask.run();
        }

        public T getResult() {
            return result;
        }
    }
}
