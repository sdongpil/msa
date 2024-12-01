package com.sparta.msa.exam.order.dto;

import lombok.Getter;

@Getter
public class OrderProductInfoDto {
    private Long productId;
    private int quantity;
    private int price;
}
