package com.sparta.msa.exam.order.dto.orderProduct;

import com.sparta.msa.exam.order.dto.OrderProductResponseDto;

public record UpdateOrderResultDto(
        boolean success,
        OrderProductResponseDto orderProductResponseDto
) {
}
