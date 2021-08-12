package com.mflyyou.cloud.common.log.strategy;

import com.github.houbb.sensitive.api.IContext;
import com.github.houbb.sensitive.api.IStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 左右各留两个个字符,剩下的全替换*
 */
public class DefaultSensitiveStrategy implements IStrategy {

    private static final String REPLACE_STRING = "**";
    private static final int REPLACE_LENGTH = REPLACE_STRING.length();

    @Override
    public Object des(Object original, IContext context) {
        if (Objects.isNull(original)) {
            return null;
        }
        final String string = original.toString();

        final int stringLength = string.length();

        // 原来字符小于前缀,直接返回 ***
        if (stringLength <= REPLACE_LENGTH * 3) {
            return REPLACE_STRING;
        }

        String prefix = StringUtils.substring(string, 0, REPLACE_LENGTH);
        String suffix = StringUtils.substring(string, -REPLACE_LENGTH);

        return prefix + REPLACE_STRING + suffix;
    }
}
