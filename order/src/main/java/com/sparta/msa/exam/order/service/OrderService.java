package com.sparta.msa.exam.order.service;

import com.sparta.msa.exam.order.domain.entity.OrderStatus;
import com.sparta.msa.exam.order.domain.entity.order.Order;
import com.sparta.msa.exam.order.domain.entity.order.OrderProduct;
import com.sparta.msa.exam.order.domain.repository.OrderProductRepository;
import com.sparta.msa.exam.order.domain.repository.OrderRepository;
import com.sparta.msa.exam.order.dto.OrderProductInfoDto;
import com.sparta.msa.exam.order.dto.OrderRequestDto;
import com.sparta.msa.exam.order.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    @Transactional
    public OrderResponseDto placeOrder(OrderRequestDto requestDto) {
        String transactionId = UUID.randomUUID().toString();

        //재고 수량 확인 준비
        prepare(requestDto, transactionId);

        // 주문 엔티티 저장
        orderSave(requestDto);

        // 재고차감 커밋
        commit(transactionId);


        return OrderResponseDto.from(order);
    }

    private void commit(String transactionId) {
        productClient.commit(transactionId);
    }

    private void orderSave(OrderRequestDto requestDto) {
        Order order = orderRepository.save(new Order(requestDto.userId(), OrderStatus.SUCCESS));

        List<OrderProductInfoDto> orderProductInfoDtoList = requestDto.orderProductList();
        for (OrderProductInfoDto item : orderProductInfoDtoList) {
            orderProductRepository.save(OrderProduct.builder()
                    .order(order)
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .totalPrice(item.getPrice() * item.getQuantity())
                    .build());
        }
    }

    private void prepare(OrderRequestDto requestDto, String transactionId) {
        Map<Long, Boolean> prepareResults = new HashMap<>();

        List<OrderProductInfoDto> orderProductInfoDtoList = requestDto.orderProductList();

        for (OrderProductInfoDto orderProductInfoDto : orderProductInfoDtoList) {
            boolean prepareStock = productClient.prepareStock(
                    orderProductInfoDto.getProductId(),
                    orderProductInfoDto.getQuantity(),
                    transactionId);

            prepareResults.put(orderProductInfoDto.getProductId(), prepareStock);

            if (!prepareStock) {
                rollback(transactionId, prepareResults);
                throw new IllegalStateException("재고 문제 발생");
            }
        }
    }

    private void rollback(String transactionId, Map<Long, Boolean> prepareResults) {
        prepareResults.forEach((productId, result) -> {
            if (result) {
                try {
                    productClient.rollbackPrepare(transactionId);
                } catch (Exception e) {
                    log.error("롤백중에 문제 발생 productId ={} ", productId);
                }
            }
        });
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
