package com.sparta.msa.exam.order.dto.order;

public record CreateOrderResultDto(
        boolean success,
        OrderResponseDto orderResponseDto
) {
}
