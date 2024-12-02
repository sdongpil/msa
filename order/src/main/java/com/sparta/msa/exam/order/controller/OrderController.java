package com.sparta.msa.exam.order.controller;

import com.sparta.msa.exam.order.domain.entity.order.Order;
import com.sparta.msa.exam.order.domain.repository.OrderRepository;
import com.sparta.msa.exam.order.dto.OrderRequestDto;
import com.sparta.msa.exam.order.dto.OrderResponseDto;
import com.sparta.msa.exam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

//    @GetMapping("/3")
//    public String test() {
//        return orderService.test();
//    }

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto requestDto) {

       OrderResponseDto responseDto = orderService.placeOrder(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(@RequestParam Long userId, Pageable pageable) {
        Page<OrderResponseDto> responseDtoList = orderService.getOrders(userId, pageable);

        return ResponseEntity.ok(responseDtoList);
    }

    @GetMapping("/orders/osiv")
    public void osivTest(@RequestParam Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        System.out.println("getOrderProductList = " + order.getOrderProductList().get(0));

    }
}
