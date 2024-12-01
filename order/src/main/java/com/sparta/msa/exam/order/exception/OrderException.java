package com.sparta.msa.exam.order.exception;

import lombok.Getter;

@Getter
public class OrderException extends RuntimeException{
    private final ErrorCode errorCode;

    public OrderException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
