package com.mflyyou.cloud.common.log.strategy;

import com.github.houbb.sensitive.annotation.metadata.SensitiveStrategy;
import com.github.houbb.sensitive.api.impl.SensitiveStrategyBuiltIn;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SensitiveStrategy(DefaultSensitiveStrategy.class)
public @interface DefaultSensitive {
}