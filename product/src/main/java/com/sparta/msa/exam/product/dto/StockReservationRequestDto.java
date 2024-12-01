package com.sparta.msa.exam.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockReservationRequestDto {
    private int quantity;
    private String transactionId;
}
