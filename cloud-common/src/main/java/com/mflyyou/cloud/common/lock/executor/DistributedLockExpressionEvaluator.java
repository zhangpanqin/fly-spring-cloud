package com.mflyyou.cloud.common.lock.executor;

import com.mflyyou.cloud.common.lock.exception.LockSpelExpressionException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DistributedLockExpressionEvaluator extends CachedExpressionEvaluator {

    private final Map<ExpressionKey, Expression> keyCache = new ConcurrentHashMap<>(64);


    public LockKeyEvaluationContext createEvaluationContext(
            Method method,
            Object[] args,
            Object target,
            Class targetClass) {

        LockExpressionRootObject lockExpressionRootObject = new LockExpressionRootObject(method, args, target, targetClass);
        LockKeyEvaluationContext evaluationContext = new LockKeyEvaluationContext(lockExpressionRootObject,
                method,
                args,
                getParameterNameDiscoverer());
        return evaluationContext;
    }

    public String key(String keyExpression,
                      Method method,
                      Object[] args,
                      Object target,
                      Class targetClass) {
        if (StringUtils.isEmpty(keyExpression)) {
            return method.toString();
        }
        LockKeyEvaluationContext evaluationContext = this.createEvaluationContext(method,
                args,
                target,
                targetClass);
        Object value;
        try {
            value = getExpression(this.keyCache, getAnnotatedElementKey(method, targetClass), keyExpression).getValue(evaluationContext);
        } catch (Exception e) {
            throw new LockSpelExpressionException(String.format("SpEL [%s] parse error in [%s]", keyExpression, method.toString()));
        }
        return Optional.ofNullable(value).map(Object::toString).orElseGet(() -> method.toString());
    }


    private AnnotatedElementKey getAnnotatedElementKey(Method method, Class targetClass) {
        return new AnnotatedElementKey(method, targetClass);
    }

    private static class LockExpressionRootObject {

        private final Method method;

        private final Object[] args;

        private final Object target;

        private final Class<?> targetClass;


        public LockExpressionRootObject(Method method,
                                        Object[] args,
                                        Object target,
                                        Class<?> targetClass) {

            this.method = method;
            this.target = target;
            this.targetClass = targetClass;
            this.args = args;
        }

        public Method getMethod() {
            return this.method;
        }

        public String getMethodName() {
            return this.method.getName();
        }

        public Object[] getArgs() {
            return this.args;
        }

        public Object getTarget() {
            return this.target;
        }

        public Class<?> getTargetClass() {
            return this.targetClass;
        }
    }

    static class LockKeyEvaluationContext extends MethodBasedEvaluationContext {
        private final Method method;

        public LockKeyEvaluationContext(Object rootObject,
                                        Method method,
                                        Object[] arguments,
                                        ParameterNameDiscoverer parameterNameDiscoverer) {
            super(rootObject, Objects.requireNonNull(method), arguments, parameterNameDiscoverer);
            this.method = method;
        }

        public Method getMethod() {
            return method;
        }
    }
}