package com.mflyyou.cloud.user.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Order_1_Advice implements MethodInterceptor {

    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        System.out.println("Order_1_Advice.invocation.proceed 之前");

        Object proceed = invocation.proceed();


        System.out.println("Order_1_Advice.invocation.proceed 之后");

        return proceed;
    }
}