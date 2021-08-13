package com.mflyyou.cloud.common.lock;

import com.mflyyou.cloud.common.lock.executor.DistributedLockExecutor;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.BeanFactory;

import java.lang.annotation.Annotation;

public class DistributedLockAdvisor extends AbstractPointcutAdvisor {

    private final Advice advice;

    private final Pointcut pointcut;

    public DistributedLockAdvisor(DistributedLockExecutor distributedLockExecutor) {
        this.advice = buildAdvice(distributedLockExecutor);
        this.pointcut = buildPointcut(DistributedLock.class);
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }


    protected Advice buildAdvice(DistributedLockExecutor distributedLockExecutor) {
        return new DistributedLockAdvice(distributedLockExecutor);
    }

    protected Pointcut buildPointcut(Class<? extends Annotation> asyncAnnotationType) {
        return new AnnotationMatchingPointcut(null, asyncAnnotationType, true);
    }
}