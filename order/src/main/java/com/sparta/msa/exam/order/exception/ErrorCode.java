package com.sparta.msa.exam.order.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    STOCK_DECREASE_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "재고 차감 실패"),
    STOCK_RESERVATION_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "재고 예약 실패"),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND,"주문 정보를 찾을 수 없습니다." );

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
