package com.mflyyou.cloud.common.exception.resolver;

import com.mflyyou.cloud.common.exception.AppException;
import com.mflyyou.cloud.common.exception.ErrorCode;
import com.mflyyou.cloud.common.lock.exception.AbstractDistributedLockAcquireException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mflyyou.cloud.common.exception.resolver.CommonErrorCode.DATA_FORMAT_INCORRECT;
import static com.mflyyou.cloud.common.exception.resolver.CommonErrorCode.REQUEST_VALIDATION_FAILED;
import static com.mflyyou.cloud.common.exception.resolver.CommonErrorCode.SERVER_ERROR;
import static com.mflyyou.cloud.common.exception.resolver.CommonErrorCode.SERVER_IS_BUSY_AND_TRY_AGAIN_LATER;
import static com.mflyyou.cloud.common.exception.resolver.CommonErrorResponse.MESSAGE;


/**
 * {@link HandlerExceptionResolver} 会处理异常,链式调用 bean 中的实现, ModelAndView 不为 null 跳出处理.
 * DefaultErrorAttributes 在 request 标记出现了异常,然后返回 null
 * 然后调用 HandlerExceptionResolverComposite 处理,它聚合了多个 HandlerExceptionResolver 处理逻辑
 * 1/HandlerExceptionResolver 返回值为 null ,经过一些列逻辑,判断 request 中标记了异常,请求转发到 /error 处理
 * 2/HandlerExceptionResolver 返回值不为 null,经过视图处理之后,清除 request 中的异常标记
 * <p>
 * 建议扩展加入到 HandlerExceptionResolverComposite
 * <p>
 * 当前实现类仅仅处理接口异常
 */
@Slf4j
public class CommonHandlerExceptionResolver extends AbstractHandlerMethodExceptionResolver {

    /**
     * 处理以下异常
     * {@link AppException}
     * {@link MethodArgumentTypeMismatchException}
     * {@link HttpMessageNotReadableException}
     * {@link MethodArgumentNotValidException}
     * {@link TypeMismatchException}
     * {@link AppException}
     */
    @Nullable
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request,
                                                           HttpServletResponse response,
                                                           @Nullable HandlerMethod handlerMethod,
                                                           Exception ex) {
        log.error("exception info log:", ex);

        /**
         * 业务异常
         */
        if (ex instanceof AppException) {
            return handleAppException((AppException) ex, request, response, handlerMethod);
        }

        /**
         * 分布式锁相关
         */
        if (ex instanceof AbstractDistributedLockAcquireException) {
            return handleAbstractDistributedLockAcquireException(request, response, handlerMethod,
                    (AbstractDistributedLockAcquireException) ex);
        }

        if (ex instanceof IllegalArgumentException) {
            return handleIllegalArgumentException(request, response, handlerMethod,
                    (IllegalArgumentException) ex);
        }
        /**
         * convert 转换异常
         */
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatchException(request, response, handlerMethod,
                    (MethodArgumentTypeMismatchException) ex);
        }

        /**
         * 读取 body 异常,转换对应的数据类型失败
         */
        if (ex instanceof HttpMessageNotReadableException) {
            return handleHttpMessageNotReadableException(request, response, handlerMethod,
                    (HttpMessageNotReadableException) ex);
        }


        if (ex instanceof TypeMismatchException) {
            return handleTypeMismatchException(request, response, handlerMethod,
                    (TypeMismatchException) ex);
        }

        /**
         * 处理 MethodArgumentNotValidException 异常
         * 参数校验通常处理 json 校验
         * public String test2(@Valid @RequestBody ValidationTestDTO validationTestDTO)
         */
        if (ex instanceof MethodArgumentNotValidException) {
            return handleMethodArgumentNotValidException(request, response, handlerMethod,
                    (MethodArgumentNotValidException) ex);
        }

        /**
         * 方法上参数校验
         * public String test1(@NotBlank(message = "name 不能为空") String name,@NotBlank(message = "name 不能为空") String age)
         */
        if (ex instanceof ConstraintViolationException) {
            return handleConstraintViolationException(request, response, handlerMethod,
                    (ConstraintViolationException) ex);
        }

        /**
         * 有一些校验异常继承了 BindException
         */
        if (ex instanceof BindException) {
            return handleBindException(request, response, handlerMethod,
                    (BindException) ex);
        }

        // 返回为 null ,后续的 HandlerExceptionResolver 有机会处理异常
        return null;
    }

    protected ModelAndView handleAbstractDistributedLockAcquireException(HttpServletRequest request,
                                                                         HttpServletResponse response,
                                                                         HandlerMethod handlerMethod,
                                                                         AbstractDistributedLockAcquireException ex) {
        ModelAndView modelAndView = new ModelAndView();
        MappingJackson2JsonView jackson2JsonView = new MappingJackson2JsonView();
        modelAndView.setView(jackson2JsonView);
        // 设置接口状态码
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        // 设置返回的 json 数据
        modelAndView.addAllObjects(buildErrorResponse(SERVER_IS_BUSY_AND_TRY_AGAIN_LATER, Map.of(MESSAGE, ex.getMessage())));
        return modelAndView;
    }

    protected ModelAndView handleMethodArgumentNotValidException(HttpServletRequest request,
                                                                 HttpServletResponse response,
                                                                 HandlerMethod handlerMethod,
                                                                 MethodArgumentNotValidException ex) {

        Map<String, Object> errorMessageMap = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField,
                        fieldError -> DetailErrorMessage
                                .builder()
                                .description(fieldError.getDefaultMessage())
                                .detailMessage(buildValidationFailedMessage(fieldError.getField(), fieldError.getRejectedValue()))
                                .build())
                );
        ModelAndView modelAndView = new ModelAndView();
        MappingJackson2JsonView jackson2JsonView = new MappingJackson2JsonView();
        modelAndView.setView(jackson2JsonView);
        // 设置接口状态码
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        // 设置返回的 json 数据
        modelAndView.addAllObjects(buildErrorResponse(REQUEST_VALIDATION_FAILED, errorMessageMap));
        return modelAndView;
    }

    protected ModelAndView handleBindException(HttpServletRequest request,
                                               HttpServletResponse response,
                                               HandlerMethod handlerMethod,
                                               BindException ex) {
        ModelAndView modelAndView = new ModelAndView();
        MappingJackson2JsonView jackson2JsonView = new MappingJackson2JsonView();
        modelAndView.setView(jackson2JsonView);
        // 设置接口状态码
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        // 设置返回的 json 数据
        modelAndView.addAllObjects(buildErrorResponse(DATA_FORMAT_INCORRECT, buildFieldErrorsMessage(ex.getFieldErrors())));
        return modelAndView;
    }

    protected ModelAndView handleHttpMessageNotReadableException(HttpServletRequest request,
                                                                 HttpServletResponse response,
                                                                 HandlerMethod handlerMethod,
                                                                 HttpMessageNotReadableException ex) {
        ModelAndView modelAndView = new ModelAndView();
        MappingJackson2JsonView jackson2JsonView = new MappingJackson2JsonView();
        modelAndView.setView(jackson2JsonView);
        // 设置接口状态码
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        // 设置返回的 json 数据
        modelAndView.addAllObjects(buildErrorResponse(DATA_FORMAT_INCORRECT, Map.of(MESSAGE, ex.getMessage())));
        return modelAndView;
    }

    protected ModelAndView handleMethodArgumentTypeMismatchException(HttpServletRequest request,
                                                                     HttpServletResponse response,
                                                                     HandlerMethod handlerMethod,
                                                                     MethodArgumentTypeMismatchException ex) {
        ModelAndView modelAndView = new ModelAndView();
        MappingJackson2JsonView jackson2JsonView = new MappingJackson2JsonView();
        modelAndView.setView(jackson2JsonView);
        // 设置接口状态码
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        // 设置返回的 json 数据
        modelAndView.addAllObjects(buildErrorResponse(DATA_FORMAT_INCORRECT,
                Map.of(MESSAGE, buildFiledErrorMessage(ex.getName(), ex.getValue()))));
        return modelAndView;
    }

    protected ModelAndView handleAppException(AppException ex,
                                              HttpServletRequest request,
                                              HttpServletResponse response,
                                              @Nullable Object handler) {

        ModelAndView modelAndView = new ModelAndView();
        MappingJackson2JsonView jackson2JsonView = new MappingJackson2JsonView();
        modelAndView.setView(jackson2JsonView);
        // 设置接口状态码
        modelAndView.setStatus(HttpStatus.valueOf(ex.getError().getStatus()));
        // 设置返回的 json 数据
        modelAndView.addAllObjects(buildErrorResponse(ex.getError(), ex.getData()));
        return modelAndView;
    }

    protected ModelAndView handleTypeMismatchException(HttpServletRequest request,
                                                       HttpServletResponse response,
                                                       HandlerMethod handlerMethod,
                                                       TypeMismatchException ex) {
        ModelAndView modelAndView = new ModelAndView();
        MappingJackson2JsonView jackson2JsonView = new MappingJackson2JsonView();
        modelAndView.setView(jackson2JsonView);
        // 设置接口状态码
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        // 设置返回的 json 数据
        modelAndView.addAllObjects(buildErrorResponse(SERVER_ERROR, buildTypeMismatchErrorMessage(ex)));
        return modelAndView;
    }

    protected Map buildErrorResponse(ErrorCode errorCode, Map<String, Object> message) {
        return CommonErrorResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .build()
                .toMap();
    }

    private ModelAndView handleIllegalArgumentException(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        HandlerMethod handlerMethod,
                                                        IllegalArgumentException ex) {
        ModelAndView modelAndView = new ModelAndView();
        MappingJackson2JsonView jackson2JsonView = new MappingJackson2JsonView();
        modelAndView.setView(jackson2JsonView);
        // 设置接口状态码
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        // 设置返回的 json 数据
        modelAndView.addAllObjects(buildErrorResponse(SERVER_ERROR, Map.of(MESSAGE, ex.getMessage())));
        return modelAndView;
    }

    private ModelAndView handleConstraintViolationException(HttpServletRequest request,
                                                            HttpServletResponse response,
                                                            HandlerMethod handlerMethod,
                                                            ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        Map<String, Object> errorMessageMap = constraintViolations.stream()
                .collect(Collectors.toMap(constraintViolation -> constraintViolation.getPropertyPath().toString(),
                        constraintViolation -> DetailErrorMessage.builder()
                                .detailMessage(buildValidationFailedMessage(constraintViolation.getPropertyPath().toString(),
                                        constraintViolation.getInvalidValue()))
                                .description(constraintViolation.getMessage())
                                .build())
                );
        ModelAndView modelAndView = new ModelAndView();
        MappingJackson2JsonView jackson2JsonView = new MappingJackson2JsonView();
        modelAndView.setView(jackson2JsonView);
        // 设置接口状态码
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        // 设置返回的 json 数据
        modelAndView.addAllObjects(buildErrorResponse(REQUEST_VALIDATION_FAILED, errorMessageMap));
        return modelAndView;
    }

    private Map<String, Object> buildTypeMismatchErrorMessage(TypeMismatchException ex) {
        return Map.of(MESSAGE, String.format("not find convert for the field [%s] to java type [%s], and value is [%s]",
                ex.getPropertyName(), ex.getRequiredType().getName(), ex.getValue()));
    }

    private Map<String, Object> buildFieldErrorsMessage(List<FieldError> fieldErrorList) {
        if (CollectionUtils.isEmpty(fieldErrorList)) {
            return Map.of();
        }
        return fieldErrorList.stream().collect(Collectors.toMap(FieldError::getField,
                fieldError -> buildFiledErrorMessage(fieldError.getField(), fieldError.getRejectedValue())));
    }

    private String buildValidationFailedMessage(String fieldName, Object fieldValue) {
        return String.format("the field [%s] is invalid, and value is [%s]", fieldName, fieldValue);
    }

    private String buildFiledErrorMessage(String fieldName, Object fieldValue) {
        return String.format("data format of the field [%s] is incorrect, and value is [%s]", fieldName, fieldValue);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    private static class DetailErrorMessage implements Serializable {
        private static final long serialVersionUID = 8144173385235349442L;
        private String description;
        private String detailMessage;

    }
}
