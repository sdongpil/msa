package com.sparta.msa.exam.order.service;

import com.sparta.msa.exam.order.domain.entity.OrderStatus;
import com.sparta.msa.exam.order.domain.entity.order.Order;
import com.sparta.msa.exam.order.domain.entity.order.OrderProduct;
import com.sparta.msa.exam.order.domain.repository.OrderProductRepository;
import com.sparta.msa.exam.order.domain.repository.OrderRepository;
import com.sparta.msa.exam.order.dto.OrderProductInfoDto;
import com.sparta.msa.exam.order.dto.OrderRequestDto;
import com.sparta.msa.exam.order.dto.OrderResponseDto;
import com.sparta.msa.exam.order.service.validator.OrderStockValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderStockValidator orderStockValidator;

    @Transactional
    public OrderResponseDto placeOrder(OrderRequestDto requestDto) {
        String transactionId = UUID.randomUUID().toString();
        boolean commitStockReservationResult = false;
        try {
            //재고 수량 확인 준비
            orderStockValidator.reserveProductStocks(requestDto, transactionId);

            // 주문 저장
            Order order = createOrder(requestDto, transactionId);

            // 주문 상품 저장
            createOrderProduct(order, requestDto);

            // 재고 차감 커밋
            commitStockReservationResult = orderStockValidator.commit(transactionId);

            //결제 로직 등등
            payment();

            order.setStatus(OrderStatus.SUCCESS);

            return OrderResponseDto.from(order);
        } catch (Exception e) {
            if (commitStockReservationResult) {
                orderStockValidator.rollbackCommittedStock(transactionId);
            }
            log.error("error message = {}", e.getMessage());
        } finally {
            orderStockValidator.deleteStockReservation(transactionId);
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "orders",
            key = "'orders:user:' + #userId + ':page:' + #pageable.pageNumber + ':size:' + #pageable.pageSize + ':sort:' + #pageable.sort")
    public Page<OrderResponseDto> getOrders(Long userId, Pageable pageable) {
        Page<Order> allByUserId = orderRepository.findAllByUserId(userId, pageable);

        return allByUserId.map(OrderResponseDto::from);
    }

    private void createOrderProduct(Order order, OrderRequestDto requestDto) {
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

    private Order createOrder(OrderRequestDto requestDto, String transactionId) {
        return orderRepository.save(
                Order.builder()
                        .userId(requestDto.userId())
                        .orderStatus(OrderStatus.PENDING)
                        .transactionId(transactionId).build()
        );
    }

    private void payment() {
        log.info("결제 성공");
//        throw new IllegalStateException("결제 에러");
    }
}
