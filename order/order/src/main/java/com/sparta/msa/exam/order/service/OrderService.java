package com.sparta.msa.exam.order.service;

import com.sparta.msa.exam.order.domain.repository.OrderRepository;
import com.sparta.msa.exam.order.dto.OrderRequestDto;
import com.sparta.msa.exam.order.dto.OrderResponseDto;
import com.sparta.msa.exam.order.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final ProductClient productClient;
    private final OrderRepository orderRepository;

    public String test() {
        return productClient.test();
    }

    public OrderResponseDto createOrder(OrderRequestDto requestDto) {
        List<Long> itemList = requestDto.productList();

        List<ProductResponseDto> productList = productClient.getProductList();

        List<Long> productList1 = requestDto.productList();


        return null;
//        orderRepository.save()
    }
}
