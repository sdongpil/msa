package com.sparta.msa.exam.order.controller;

import com.sparta.msa.exam.order.dto.order.*;
import com.sparta.msa.exam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateRequestDto requestDto) {
        CreateOrderResultDto response = orderService.placeOrder(requestDto);

        return response.success() ?
                ResponseEntity.ok(response.orderResponseDto()) :
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("잠시 후에 주문 추가를 요청 해주세요");
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(@RequestParam Long userId, Pageable pageable) {
        List<OrderResponseDto> responseDtoList = orderService.getOrders(userId, pageable);

        return ResponseEntity.ok(responseDtoList);
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @RequestBody OrderUpdateRequestDto requestDto) {
        UpdateOrderResultDto response = orderService.updateOrder(orderId, requestDto);

        return response.success() ?
                ResponseEntity.ok(response.orderProductResponseDto()) :
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("잠시 후에 주문 수정을 요청 해주세요");
    }
}
