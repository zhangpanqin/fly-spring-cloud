package com.mflyyou.cloud.exception;

import com.mflyyou.cloud.common.exception.AccessDeniedAppException;
import com.mflyyou.cloud.common.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;
import static org.springframework.http.HttpStatus.valueOf;

@Slf4j
@ControllerAdvice
@ConditionalOnWebApplication(type = SERVLET)
public class GlobalExceptionHandler {
    /**
     * 重写了 common 中定义的某个异常处理
     */
    @ExceptionHandler(AccessDeniedAppException.class)
    @ResponseBody
    public ResponseEntity handleAppException(AppException ex, HttpServletRequest request) {
        return new ResponseEntity<>(Map.of("1", "2"), valueOf(ex.getError().getStatus()));
    }
}