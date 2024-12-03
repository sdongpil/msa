package com.sparta.msa.exam.order.dto;

import com.sparta.msa.exam.order.domain.entity.order.OrderProduct;

public record OrderProductResponseDto(
        Long productId,
        int quantity,
        int price
) {
    public static OrderProductResponseDto from(OrderProduct orderProduct) {
        return new OrderProductResponseDto(
                orderProduct.getProductId(),
                orderProduct.getQuantity(),
                orderProduct.getTotalPrice());
    }
}
