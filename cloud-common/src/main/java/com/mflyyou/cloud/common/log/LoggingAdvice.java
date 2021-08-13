package com.mflyyou.cloud.common.log;


import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class LoggingAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Stopwatch watch = Stopwatch.createStarted();
        if (log.isInfoEnabled()) {
            log.info("Entering method {}.{}({})",
                    invocation.getMethod().getDeclaringClass().getName(),
                    invocation.getMethod().getName(),
                    getArgumentsString(invocation.getArguments()));
        }
        try {
            Object result = invocation.proceed();
            if (log.isInfoEnabled()) {
                log.info("Exiting method {}.{}; execution time: {}ms; response: {};",
                        invocation.getMethod().getDeclaringClass().getName(),
                        invocation.getMethod().getName(),
                        watch.elapsed(TimeUnit.MILLISECONDS),
                        desensitizeCopy(result)
                );
            }
            return result;
        } catch (Throwable ex) {
            log.error("Exiting method {}.{}; execution time: {}ms; exec failed;",
                    invocation.getMethod().getDeclaringClass().getName(),
                    invocation.getMethod().getName(),
                    watch.elapsed(TimeUnit.MILLISECONDS),
                    ex);
            throw ex;
        }
    }

    private String desensitizeCopy(Object originalLogInfo) {
        if (originalLogInfo==null) {
            return null;
        }
        try {
            return SensitiveUtil.desJson(originalLogInfo);
        } catch (Exception e) {
            return originalLogInfo.getClass().toString();
        }
    }

    private String getArgumentsString(Object... arguments) {
        return Arrays.stream(arguments).map(argument -> {
            try {
                return desensitizeCopy(argument);
            } catch (Throwable e) {
                return "";
            }
        }).collect(Collectors.joining(","));
    }
}