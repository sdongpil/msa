package com.sparta.msa.exam.order.dto.order;

import com.sparta.msa.exam.order.dto.orderProduct.OrderProductResponseDto;

public record UpdateOrderResultDto(
        boolean success,
        OrderProductResponseDto orderProductResponseDto
) {
}
