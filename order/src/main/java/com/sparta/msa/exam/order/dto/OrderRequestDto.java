package com.sparta.msa.exam.order.dto;

import java.util.List;

public record OrderRequestDto (
        List<OrderProductInfoDto> orderProductList,
        Long userId
        ) {
}
