package com.mflyyou.cloud.common.exception;

public interface ErrorCode {
    int getStatus();

    String getMessage();

    String getCode();
}