package com.mflyyou.cloud.common.log;

import com.mflyyou.cloud.common.log.annotation.Loggable;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import java.lang.annotation.Annotation;

public class LoggingAdvisor extends AbstractPointcutAdvisor {

    private final Advice advice;

    private final Pointcut pointcut;

    public LoggingAdvisor() {
        this.advice = buildAdvice();
        this.pointcut = buildPointcut(Loggable.class);
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }


    protected Advice buildAdvice() {
        return new LoggingAdvice();
    }

    protected Pointcut buildPointcut(Class<? extends Annotation> asyncAnnotationType) {
        Pointcut cpc = new AnnotationMatchingPointcut(asyncAnnotationType, true);
        Pointcut mpc = new AnnotationMatchingPointcut(null, asyncAnnotationType, true);
        return new ComposablePointcut(cpc).union(mpc);
    }
}
