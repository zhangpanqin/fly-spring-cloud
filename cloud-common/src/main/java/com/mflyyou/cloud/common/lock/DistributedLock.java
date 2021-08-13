package com.mflyyou.cloud.common.lock;

import com.mflyyou.cloud.common.lock.exception.DistributedLockAcquireTimeoutException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 锁的唯一标识.
     * 支持 SpEL 表达式
     * <p>e.g. "#{#param.id}", "lock#{#id}", "lock#{1+1}"</p>
     */
    String value();

    /**
     * Lock type
     */
    LockType type() default LockType.SIMPLE_LOCK;

    /**
     * 最大等待多长时间获取锁,超时没有获取到锁,抛出异常 {@link DistributedLockAcquireTimeoutException}
     * 如果指定值 <=0 ,强制设置 5 毫秒
     * 默认 200 毫秒
     */
    long waitTime() default 200L;

    /**
     * 获取到锁之后,能维持锁的时间,不会自动续期,超时锁会自动释放,并抛出异常
     * 当设置值 <=0 的时候,锁会自动续期,最大和 redisson 配置(默认 30 分钟)有关
     */
    long leaseTime() default 30_000L;

    /**
     * 是否在锁的标识中拼接 spring.application.name 和激活的配置属性名称
     */
    boolean appName() default true;

    /**
     * 标识获取的锁类型,由 Redisson 提供
     */
    enum LockType {
        /**
         * A lock type provided by Redisson.
         */
        SIMPLE_LOCK,

        /**
         * A lock type provided by Redisson.
         * Difference from SIMPLE_LOCK, it guarantees that threads will acquire it in is same order they requested it
         */
        FAIR_LOCK,

        /**
         * A lock type provided by Redisson.
         * Multiple ReadLock owners and only one WriteLock owner are allowed.
         * <p>A client can hold multiple read locks with key A</p>
         */
        READ_LOCK,

        /**
         * A lock type provided by Redisson.
         * Multiple ReadLock owners and only one WriteLock owner are allowed.
         * <p>If client has write lock with key A, it can not acquire read lock with key A</p>
         */
        WRITE_LOCK,
    }

    class LeaseTime {
        // 传入 LeaseTime 小于等于这个值,一直会持有这个锁,不会自动释放锁,客户端链接奔溃了,设置的 key 过期时间会自动删除这个锁标识
        public final static long INFINITE = 0L;
    }

    class WaitTime {
        // waitTime 小于等于这个值时,最少等待 5 毫秒
        public final static long NO_WAIT = 0L;
    }
}
