package com.mflyyou.cloud.common.lock.executor;

import com.mflyyou.cloud.common.lock.DistributedLock.LockType;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.env.Environment;

import java.util.Objects;
import java.util.Optional;

public abstract class AbstractDistributedLockExecutor implements DistributedLockExecutor {
    private static final String PREFIX_LOCK_NAME = "LOCK_WITH_REDISSON";
    private static final String DEFAULT_APPLICATION_NAME = "unknown";
    protected final RedissonClient redissonClient;
    protected final Environment environment;


    public AbstractDistributedLockExecutor(RedissonClient redissonClient, Environment environment) {
        this.environment = Objects.requireNonNull(environment);
        this.redissonClient = Objects.requireNonNull(redissonClient);
    }

    protected RLock getLockFromLockType(String key, LockType type, boolean addAppNameAndEnv) {
        switch (type) {
            case SIMPLE_LOCK:
                return redissonClient.getLock(getLockName(key, addAppNameAndEnv));
            case FAIR_LOCK:
                return redissonClient.getFairLock(getLockName(key, addAppNameAndEnv));
            case READ_LOCK:
                return redissonClient.getReadWriteLock(getLockName(key, addAppNameAndEnv)).readLock();
            case WRITE_LOCK:
                return redissonClient.getReadWriteLock(getLockName(key, addAppNameAndEnv)).writeLock();
            default:
                throw new UnsupportedOperationException("Unsupported lock type " + type);
        }
    }

    private String getLockName(String key, boolean addAppNameAndEnv) {
        key = Optional.ofNullable(key).orElse("");
        if (addAppNameAndEnv) {
            // 为了热更新可以改变
            String applicationName = environment.getProperty("spring.application.name", DEFAULT_APPLICATION_NAME);
            String activeProfile = String.join("_", environment.getActiveProfiles());
            return String.join(":", PREFIX_LOCK_NAME, applicationName, activeProfile, key);
        }
        return String.join(":", PREFIX_LOCK_NAME, key);
    }
}
