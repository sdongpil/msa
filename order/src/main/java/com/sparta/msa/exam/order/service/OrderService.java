package com.sparta.msa.exam.order.service;

import com.sparta.msa.exam.order.domain.entity.order.Order;
import com.sparta.msa.exam.order.domain.repository.OrderRepository;
import com.sparta.msa.exam.order.dto.OrderRequestDto;
import com.sparta.msa.exam.order.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderStockValidator orderStockValidator;

    @Transactional
    public OrderResponseDto placeOrder(OrderRequestDto requestDto) {
        String transactionId = UUID.randomUUID().toString();

        //재고 수량 확인 준비
        orderStockValidator.reserveProductStocks(requestDto, transactionId);

        // 주문 정보 저장
        Order order = orderStockValidator.orderInfoSave(requestDto);
            // 주문 저장
            Order order = createOrder(requestDto, transactionId);

        // 재고 차감 커밋
        orderStockValidator.commit(transactionId);

        return OrderResponseDto.from(order);
    }


    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "orders",
            key = "'orders:user:' + #userId + ':page:' + #pageable.pageNumber + ':size:' + #pageable.pageSize + ':sort:' + #pageable.sort")
    public Page<OrderResponseDto> getOrders(Long userId, Pageable pageable) {
        Page<Order> allByUserId = orderRepository.findAllByUserId(userId, pageable);

        return allByUserId.map(OrderResponseDto::from);
    }
    private Order createOrder(OrderRequestDto requestDto, String transactionId) {
        return orderRepository.save(
                Order.builder()
                        .userId(requestDto.userId())
                        .orderStatus(OrderStatus.PENDING)
                        .transactionId(transactionId).build()
        );
    }
}
