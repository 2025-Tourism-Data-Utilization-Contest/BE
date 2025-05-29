package com.saerok.showing.api.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import java.util.List;
import lombok.Getter;

@Getter
public class ErrorResponse<T> {

    private final Status status;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<T> results;

    // 에러 응답 (ErrorCode 사용)
    public static <T> ErrorResponse<T> error(ErrorCode errorCode) {
        return new ErrorResponse<>(errorCode);
    }

    // 에러 응답 (ChaeumException 사용)
    public static <T> ErrorResponse<T> error(ShowingException exception) {
        return new ErrorResponse<>(exception.getErrorCode());
    }

    public ErrorResponse(ErrorCode errorCode) {
        this.status = new Status(errorCode);
        this.results = List.of();
    }

    @Getter
    private static class Status {
        private final int code;       // HTTP 상태 코드
        private final String message; // 상태 메시지

        public Status(ErrorCode errorCode) {
            this.code = errorCode.getStatus().value();
            this.message = errorCode.getMessage();
        }
    }
}
