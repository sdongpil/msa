package com.sparta.msa.exam.order.service;

import com.sparta.msa.exam.order.domain.entity.order.Order;
import com.sparta.msa.exam.order.domain.entity.order.OrderProduct;
import com.sparta.msa.exam.order.domain.repository.OrderRepository;
import com.sparta.msa.exam.order.dto.OrderRequestDto;
import com.sparta.msa.exam.order.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        // 재고 차감 커밋
        orderStockValidator.commit(transactionId);

        return OrderResponseDto.from(order);
    }



    //    @Transactional(readOnly = true)
    public void getOrders(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        for (Order order : orders) {
            OrderProduct orderProduct = order.getOrderProductList().get(0);
            log.info("orderProduct ={}", orderProduct);
        }
    }
}
