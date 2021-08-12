package com.mflyyou.cloud.common.exception.resolver;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

import static com.mflyyou.cloud.common.exception.resolver.CommonErrorCode.SERVER_ERROR;

@Configuration
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@EnableConfigurationProperties({ ServerProperties.class})
public class CommonErrorControllerConfiguration {
    @Controller
    @RequestMapping("${server.error.path:${error.path:/error}}")
    static class CommonErrorController extends AbstractErrorController {

        private final ErrorAttributes errorAttributes;

        private final ErrorProperties errorProperties;

        public CommonErrorController(ErrorAttributes errorAttributes,ServerProperties serverProperties) {
            super(errorAttributes, Collections.emptyList());
            this.errorAttributes = errorAttributes;
            this.errorProperties = serverProperties.getError();
        }

        @RequestMapping
        public ResponseEntity<CommonErrorResponse> error(HttpServletRequest request) {
            HttpStatus status = getStatus(request);
            if (status==HttpStatus.NO_CONTENT) {
                return new ResponseEntity<>(status);
            }

            return new ResponseEntity<>(CommonErrorResponse.builder()
                    .errorCode(SERVER_ERROR)
                    .message(getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL)))
                    .build(), status);
        }

        protected ErrorProperties getErrorProperties() {
            return this.errorProperties;
        }

        protected Map<String, Object> getErrorAttributes(HttpServletRequest request, ErrorAttributeOptions options) {
            WebRequest webRequest = new ServletWebRequest(request);
            return this.errorAttributes.getErrorAttributes(webRequest, options);
        }

        protected ErrorAttributeOptions getErrorAttributeOptions(HttpServletRequest request, MediaType mediaType) {
            ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
            if (this.errorProperties.isIncludeException()) {
                options = options.including(Include.EXCEPTION);
            }
            if (isIncludeStackTrace(request, mediaType)) {
                options = options.including(Include.STACK_TRACE);
            }
            if (isIncludeMessage(request, mediaType)) {
                options = options.including(Include.MESSAGE);
            }
            if (isIncludeBindingErrors(request, mediaType)) {
                options = options.including(Include.BINDING_ERRORS);
            }
            return options;
        }

        protected boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces) {
            switch (getErrorProperties().getIncludeStacktrace()) {
                case ALWAYS:
                    return true;
                case ON_PARAM:
                    return getTraceParameter(request);
                default:
                    return false;
            }
        }

        protected boolean isIncludeMessage(HttpServletRequest request, MediaType produces) {
            switch (getErrorProperties().getIncludeMessage()) {
                case ALWAYS:
                    return true;
                case ON_PARAM:
                    return getMessageParameter(request);
                default:
                    return false;
            }
        }

        protected boolean isIncludeBindingErrors(HttpServletRequest request, MediaType produces) {
            switch (getErrorProperties().getIncludeBindingErrors()) {
                case ALWAYS:
                    return true;
                case ON_PARAM:
                    return getErrorsParameter(request);
                default:
                    return false;
            }
        }
    }
}