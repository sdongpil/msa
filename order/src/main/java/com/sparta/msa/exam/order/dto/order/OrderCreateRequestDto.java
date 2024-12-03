package com.sparta.msa.exam.order.dto.order;

import com.sparta.msa.exam.order.dto.orderProduct.OrderProductInfoDto;

import java.util.List;

public record OrderCreateRequestDto(
        List<OrderProductInfoDto> orderProductList,
        Long userId
        ) {
}
