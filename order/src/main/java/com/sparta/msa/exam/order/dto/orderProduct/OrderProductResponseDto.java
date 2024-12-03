package com.sparta.msa.exam.order.dto.orderProduct;

public record OrderProductResponseDto(
        Long productId,
        int quantity,
        int price
) {

}
