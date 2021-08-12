package com.mflyyou.cloud.common.exception.resolver;

import com.mflyyou.cloud.common.exception.ErrorCode;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@Getter
@EqualsAndHashCode
@ToString
public class CommonErrorResponse implements Serializable {
    static final String MESSAGE = "message";
    private static final long serialVersionUID = 807505212597992579L;
    private static final String CODE = "code";
    private static final String CODE_DESCRIPTION = "codeDescription";
    private String code;
    private String codeDescription;
    private Map message;

    public CommonErrorResponse(@NotNull ErrorCode errorCode, @NotNull Map message) {
        Objects.requireNonNull(errorCode);
        Objects.requireNonNull(message);
        this.message = message;
        this.code = errorCode.getCode();
        this.codeDescription = errorCode.getMessage();
    }

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

    public Map toMap() {
        return Map.of(CODE, code, CODE_DESCRIPTION, codeDescription, MESSAGE, message);
    }

    public static class ErrorResponseBuilder {

        private ErrorCode errorCode;

        private Map message;

        public ErrorResponseBuilder errorCode(ErrorCode errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public ErrorResponseBuilder message(Map message) {
            this.message = message;
            return this;
        }


        @Generated
        public CommonErrorResponse build() {
            return new CommonErrorResponse(this.errorCode, this.message);
        }

        @Override
        public String toString() {
            return "ErrorResponseBuilder{" +
                    "errorCode=" + errorCode +
                    ", message=" + message +
                    '}';
        }
    }
}
