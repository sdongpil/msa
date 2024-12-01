package com.sparta.msa.exam.order.dto;

import com.sparta.msa.exam.order.domain.entity.OrderStatus;
import com.sparta.msa.exam.order.domain.entity.order.Order;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderResponseDto {
    private Long orderId;
    private LocalDateTime createdAt;
    private OrderStatus orderStatus;

    public static OrderResponseDto from(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .createdAt(order.getCreatedAt())
                .orderStatus(order.getOrderStatus())
                .build();
    }
}


