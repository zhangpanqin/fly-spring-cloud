package com.mflyyou.cloud.common.constant;

import org.springframework.core.Ordered;

/**
 * order 值越大越最后执行
 */
public class AopOrderConstant {
    // 日志切面顺序,优先级最高
    public static final int LOGGING_AOP_CONSTANT = Ordered.HIGHEST_PRECEDENCE + 100;

    // 分布式锁日志页面,优先级最低
    public static final int DISTRIBUTED_LOCK_AOP_CONSTANT = Ordered.LOWEST_PRECEDENCE;

    // 分布式锁日志页面,优先级最低
    public static final int TRANSACTION_AOP_CONSTANT = Ordered.LOWEST_PRECEDENCE-1;
}
