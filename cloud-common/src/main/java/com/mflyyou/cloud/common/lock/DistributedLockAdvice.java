package com.mflyyou.cloud.common.lock;


import com.mflyyou.cloud.common.lock.exception.DistributedLockTaskException;
import com.mflyyou.cloud.common.lock.executor.DistributedLockExecutor;
import com.mflyyou.cloud.common.lock.executor.DistributedLockExpressionEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@Slf4j
public class DistributedLockAdvice implements MethodInterceptor {
    private final DistributedLockExpressionEvaluator evaluator = new DistributedLockExpressionEvaluator();
    private DistributedLockExecutor distributedLockExecutor;

    public DistributedLockAdvice(DistributedLockExecutor distributedLockExecutor) {
        this.distributedLockExecutor = distributedLockExecutor;
    }


    @Override
    public Object invoke(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        DistributedLock annotation = method.getAnnotation(DistributedLock.class);
        return distributedLockExecutor.execute(getCallable(invocation),
                getLockName(annotation,
                        method,
                        invocation.getArguments(),
                        invocation.getThis(),
                        invocation.getThis().getClass()),
                annotation.type(),
                annotation.waitTime(),
                annotation.leaseTime(),
                annotation.appName());

    }


    public Callable getCallable(MethodInvocation invocation) {
        return () -> {
            try {
                return invocation.proceed();
            } catch (Throwable throwable) {
                throw new DistributedLockTaskException(
                        String.format("method [s%] exec error in DistributedLock locking", invocation.getMethod()),
                        throwable);
            }
        };
    }

    private String getLockName(DistributedLock annotation,
                               Method method,
                               Object[] arguments,
                               Object target,
                               Class targetClass) {
        return evaluator.key(annotation.value(),
                method,
                arguments,
                target,
                targetClass
        );
    }
}