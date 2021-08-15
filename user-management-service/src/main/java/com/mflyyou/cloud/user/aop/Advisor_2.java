package com.mflyyou.cloud.user.aop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;

@Configuration(proxyBeanMethods = false)
public class Advisor_2 extends AbstractPointcutAdvisor {
    private final Advice advice;

    private final Pointcut pointcut;

    public Advisor_2() {
        this.advice = buildAdvice();
        this.pointcut = buildPointcut(Order2.class);
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
        return new Order_2_Advice();
    }

    protected Pointcut buildPointcut(Class<? extends Annotation> asyncAnnotationType) {
        return new AnnotationMatchingPointcut(null, asyncAnnotationType, true);
    }
}
