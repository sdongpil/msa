package com.sparta.msa.exam.order.service.mapper;

import com.sparta.msa.exam.order.domain.entity.OrderStatus;
import com.sparta.msa.exam.order.domain.entity.order.Order;
import com.sparta.msa.exam.order.domain.entity.order.OrderProduct;
import com.sparta.msa.exam.order.dto.order.OrderCreateRequestDto;
import com.sparta.msa.exam.order.dto.order.OrderResponseDto;
import com.sparta.msa.exam.order.dto.order.OrderUpdateRequestDto;
import com.sparta.msa.exam.order.dto.orderProduct.OrderProductInfoDto;
import com.sparta.msa.exam.order.dto.orderProduct.OrderProductResponseDto;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public OrderResponseDto toResponseDto(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .createdAt(order.getCreatedAt())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    public Order mapToOrder(OrderCreateRequestDto requestDto, String transactionId) {
        return Order.builder()
                .userId(requestDto.userId())
                .orderStatus(OrderStatus.PENDING)
                .transactionId(transactionId)
                .build();
    }

    public OrderProduct mapToOrderProduct(Order order, OrderProductInfoDto item) {
        return OrderProduct.builder()
                .order(order)
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .totalPrice(item.getPrice() * item.getQuantity())
                .build();
    }

    public OrderProduct mapToOrderProduct(Order order, OrderUpdateRequestDto requestDto) {
        return OrderProduct.builder()
                .order(order)
                .productId(requestDto.productId())
                .quantity(requestDto.quantity())
                .totalPrice(requestDto.price() * requestDto.quantity())
                .build();
    }

    public OrderProductResponseDto mapToOrderProductResponseDto(OrderProduct orderProduct) {
        return new OrderProductResponseDto(
                orderProduct.getProductId(),
                orderProduct.getQuantity(),
                orderProduct.getTotalPrice());
    }
}
