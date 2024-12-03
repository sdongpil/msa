package com.sparta.msa.exam.order.dto.order;

public record OrderUpdateRequestDto(
        Long productId,
        int quantity,
        int price
) {
}
