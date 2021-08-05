package com.mflyyou.cloud.common.util;

import feign.querymap.BeanQueryMapEncoder;

import java.util.HashMap;
import java.util.Map;

public class FeignParameterUtils {

    private static final BeanQueryMapEncoder beanQueryMapEncoder = new BeanQueryMapEncoder();

    public static Map<String, Object> toRequestMap(Object... objects) {
        Map<String, Object> resultMap = new HashMap<>();
        for (Object object : objects) {
            resultMap.putAll(beanQueryMapEncoder.encode(object));
        }
        return resultMap;
    }
}
