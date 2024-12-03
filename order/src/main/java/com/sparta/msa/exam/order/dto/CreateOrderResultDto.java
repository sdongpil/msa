package com.sparta.msa.exam.order.dto;

public record CreateOrderResultDto(
        boolean success,
        OrderResponseDto orderResponseDto
) {
}
