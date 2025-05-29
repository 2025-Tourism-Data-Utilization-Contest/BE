package com.saerok.showing.api.global.exception;

import lombok.Getter;

@Getter
public class ShowingException extends RuntimeException {

    private final ErrorCode errorCode;
    private String message;

    // 기본 생성자 (에러 코드만 전달)
    private ShowingException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 커스텀 메시지를 포함하는 생성자
    private ShowingException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    // 에러 코드만으로 예외 생성
    public static ShowingException from(ErrorCode errorCode) {
        return new ShowingException(errorCode);
    }

    // 에러 코드와 커스텀 메시지로 예외 생성
    public static ShowingException from(ErrorCode errorCode, String message) {
        return new ShowingException(errorCode, message);
    }
}
