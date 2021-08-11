package com.mflyyou.cloud.common.exception;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.collections4.MapUtils.isEmpty;

/**
 * Base exception class for all business exceptions
 */
public abstract class AppException extends RuntimeException {
    private final ErrorCode error;
    private final Map<String, Object> data = new HashMap<>();

    protected AppException(ErrorCode error, Map<String, Object> data) {
        super(format(error.getCode(), error.getMessage(), data));
        this.error = error;
        if (!isEmpty(data)) {
            this.data.putAll(data);
        }
    }

    protected AppException(ErrorCode code, Map<String, Object> data, Throwable cause) {
        super(format(code.toString(), code.getMessage(), data), cause);
        this.error = code;
        if (!isEmpty(data)) {
            this.data.putAll(data);
        }
    }

    private static String format(String code, String message, Map<String, Object> data) {
        return String.format("[%s]%s:%s.", code, message, isEmpty(data) ? "" : data.toString());
    }

    public ErrorCode getError() {
        return error;
    }

    public Map<String, Object> getData() {
        return data;
    }
}