package com.sparta.msa.exam.order.dto;

import com.sparta.msa.exam.order.domain.entity.order.Order;

public record CommitResponseDto(
        Order order,
        boolean commitStockReservationResult
) {
}
