package com.sparta.msa.exam.order.dto.orderProduct;

import lombok.Getter;

@Getter
public class OrderProductInfoDto {
    private Long productId;
    private int quantity;
    private int price;
}
