package com.mflyyou.cloud.common.exception.resolver;

import com.mflyyou.cloud.common.exception.ErrorCode;

public enum CommonErrorCode implements ErrorCode {

    ACCESS_DENIED(403, "Access denied."),

    DATA_FORMAT_INCORRECT(400, "Data format incorrect"),

    REQUEST_VALIDATION_FAILED(400, "Data is invalid and the check failed"),

    SERVER_ERROR(500, "Server error not resolve"),

    SERVER_IS_BUSY_AND_TRY_AGAIN_LATER(500, "Server is busy and try again later");


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