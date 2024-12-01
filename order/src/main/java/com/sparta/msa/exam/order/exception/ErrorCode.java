package com.sparta.msa.exam.order.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    PRODUCT_CALL_FAIL(HttpStatus.SERVICE_UNAVAILABLE, "잠시 후에 주문 추가를 요청 해주세요.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
