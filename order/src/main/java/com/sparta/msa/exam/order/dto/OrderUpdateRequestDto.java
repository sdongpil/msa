package com.sparta.msa.exam.order.dto;

public record OrderUpdateRequestDto(
        Long productId,
        int quantity,
        int price
) {
}
