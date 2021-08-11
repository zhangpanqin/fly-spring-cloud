package com.mflyyou.cloud.common.exception.resolver;

import com.mflyyou.cloud.common.exception.ErrorCode;

public enum CommonErrorCode implements ErrorCode {

    ACCESS_DENIED(403, "Access denied."),

    DATA_FORMAT_INCORRECT(400, "Data format incorrect"),

    SERVER_ERROR(500, "Server error not resolve");


    private final int status;
    private final String message;

    CommonErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return this.name();
    }
}