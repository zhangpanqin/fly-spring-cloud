package com.mflyyou.cloud.common.exception;

import java.util.Map;

import static com.mflyyou.cloud.common.exception.resolver.CommonErrorCode.ACCESS_DENIED;


public class AccessDeniedAppException extends AppException {

    public AccessDeniedAppException(Map<String, Object> data) {
        super(ACCESS_DENIED, data);
    }
}
