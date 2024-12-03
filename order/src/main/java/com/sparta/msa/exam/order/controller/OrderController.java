package com.sparta.msa.exam.order.controller;

import com.sparta.msa.exam.order.dto.CreateOrderResultDto;
import com.sparta.msa.exam.order.dto.OrderRequestDto;
import com.sparta.msa.exam.order.dto.OrderResponseDto;
import com.sparta.msa.exam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto requestDto) {
        CreateOrderResultDto response = orderService.placeOrder(requestDto);

        return response.success() ?
                ResponseEntity.ok(response.orderResponseDto()) :
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("잠시 후에 주문 추가를 요청 해주세요");
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(@RequestParam Long userId, Pageable pageable) {
        Page<OrderResponseDto> responseDtoList = orderService.getOrders(userId, pageable);

        return ResponseEntity.ok(responseDtoList);
    }
}
