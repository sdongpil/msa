package com.sparta.msa.exam.order.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorResponse> handleOrderException(OrderException e) {
        return ResponseEntity.status(e.getErrorCode()
                        .getStatus())
                .body(new ErrorResponse(e.getErrorCode().getStatus().value(), e.getMessage()));
    }


}