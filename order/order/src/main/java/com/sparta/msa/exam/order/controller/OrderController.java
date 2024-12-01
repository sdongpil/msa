package com.sparta.msa.exam.order.controller;

import com.sparta.msa.exam.order.dto.OrderRequestDto;
import com.sparta.msa.exam.order.dto.OrderResponseDto;
import com.sparta.msa.exam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/3")
    public String test() {
        return orderService.test();
    }

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto requestDto) {

       OrderResponseDto responseDto = orderService.createOrder(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
