package com.mflyyou.cloud.common.lock;


import com.mflyyou.cloud.common.lock.DistributedLockExpressionEvaluator.LockKeyEvaluationContext;
import com.mflyyou.cloud.common.lock.exception.DistributedLockTaskException;
import com.mflyyou.cloud.common.lock.executor.DistributedLockExecutor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.expression.AnnotatedElementKey;

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
        Callable callable = () -> {
            try {
                return invocation.proceed();
            } catch (Throwable throwable) {
                throw new DistributedLockTaskException(String.format("method [s%] exec error in DistributedLock locking", invocation.getMethod()),
                        throwable);
            }
        };
        DistributedLock annotation = getAnnotation(invocation.getMethod());
        return distributedLockExecutor.execute(callable,
                getLockName(invocation, annotation),
                annotation.type(),
                annotation.waitTime(),
                annotation.leaseTime(),
                annotation.appName());

    }

    private String getLockName(MethodInvocation invocation, DistributedLock annotation) {
        LockKeyEvaluationContext evaluationContext = evaluator.createEvaluationContext(invocation.getMethod(),
                invocation.getArguments(),
                invocation.getThis(),
                invocation.getThis().getClass());
        return evaluator.key(annotation.value(), getAnnotatedElementKey(invocation), evaluationContext);
    }

    private DistributedLock getAnnotation(Method method) {
        return method.getAnnotation(DistributedLock.class);
    }

    private AnnotatedElementKey getAnnotatedElementKey(MethodInvocation invocation) {
        return new AnnotatedElementKey(invocation.getMethod(), invocation.getClass());
    }
}