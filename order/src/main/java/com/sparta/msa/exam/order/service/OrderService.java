package com.sparta.msa.exam.order.service;

import com.sparta.msa.exam.order.domain.entity.OrderStatus;
import com.sparta.msa.exam.order.domain.entity.order.Order;
import com.sparta.msa.exam.order.domain.entity.order.OrderProduct;
import com.sparta.msa.exam.order.domain.repository.OrderProductRepository;
import com.sparta.msa.exam.order.domain.repository.OrderRepository;
import com.sparta.msa.exam.order.dto.order.*;
import com.sparta.msa.exam.order.dto.orderProduct.OrderProductInfoDto;
import com.sparta.msa.exam.order.exception.ErrorCode;
import com.sparta.msa.exam.order.exception.OrderException;
import com.sparta.msa.exam.order.service.mapper.OrderMapper;
import com.sparta.msa.exam.order.service.validator.OrderStockValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.sparta.msa.exam.order.exception.ErrorCode.STOCK_DECREASE_FAILED;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderStockValidator orderStockValidator;
    private final OrderMapper orderMapper;

    @Transactional
    public CreateOrderResultDto placeOrder(OrderCreateRequestDto requestDto) {
        String transactionId = UUID.randomUUID().toString();
        boolean commitStockReservationResult = false;
        Order order = null;
        try {
            //재고 수량 확인 준비
            orderStockValidator.reserveProductStocks(requestDto, transactionId);

            // 주문 저장
            order = createOrder(requestDto, transactionId);

            // 주문 상품 저장
            createOrderProduct(order, requestDto);

            // 재고 차감 커밋
            commitStockReservationResult = orderStockValidator.commit(transactionId);

            payment();

            order.setStatus(OrderStatus.SUCCESS);
            return new CreateOrderResultDto(true, orderMapper.toResponseDto(order));
        } catch (Exception e) {
            log.error("주문 실패 placeOrder() error message = {}", e.getMessage());
            if (order != null) {
                order.setStatus(OrderStatus.FAIL);
            }

            handlingOrderFailure(commitStockReservationResult, transactionId);
        } finally {
            try {
                orderStockValidator.deleteStockReservation(transactionId);
            } catch (Exception e) {
                log.error("재고 예약 정보 삭제 실패 deleteStockReservation() transactionId: {}", transactionId);
            }
        }
        return new CreateOrderResultDto(false, null);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "orders",
            key = "'orders:user:' + #userId + ':page:' + #pageable.pageNumber + ':size:' + #pageable.pageSize + ':sort:' + #pageable.sort", cacheManager = "orderCacheManager")
    public List<OrderResponseDto> getOrders(Long userId, Pageable pageable) {
        Page<Order> allByUserId = orderRepository.findAllByUserId(userId, pageable);

        return allByUserId.map(orderMapper::toResponseDto).getContent();
    }

    @Transactional
    public UpdateOrderResultDto updateOrder(Long orderId, OrderUpdateRequestDto requestDto) {
        String transactionId = UUID.randomUUID().toString();
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderException(ErrorCode.ORDER_NOT_FOUND));
        OrderProduct orderProduct = null;
        boolean commitStockReservationResult = false;

        try {
            //재고 수량 확인 준비
            orderStockValidator.reserveProductStocks(requestDto, transactionId);

            // 재고 차감 커밋
            commitStockReservationResult = orderStockValidator.commit(transactionId);
            if (!commitStockReservationResult) {
                throw new OrderException(STOCK_DECREASE_FAILED);
            }

            // 같은 상품이면 수량,가격 수정
            UpdateOrderResultDto item = updateOrderProduct(requestDto, order);
            if (item != null) return item;

            // 상품 추가
            orderProduct = addOrderProduct(requestDto, order);

            return new UpdateOrderResultDto(true, orderMapper.mapToOrderProductResponseDto(orderProduct));
        } catch (Exception e) {
            log.error("주문 실패 placeOrder() error message = {}", e.getMessage());
            handlingOrderFailure(commitStockReservationResult, transactionId);
        } finally {
            try {
                orderStockValidator.deleteStockReservation(transactionId);
            } catch (Exception e) {
                log.error("재고 예약 정보 삭제 실패 deleteStockReservation() transactionId: {}", transactionId);
            }
        }
        return new UpdateOrderResultDto(false, null);
    }

    private OrderProduct addOrderProduct(OrderUpdateRequestDto requestDto, Order order) {
        return orderProductRepository.save(orderMapper.mapToOrderProduct(order, requestDto));
    }

    private UpdateOrderResultDto updateOrderProduct(OrderUpdateRequestDto requestDto, Order order) {
        List<OrderProduct> orderProductList = orderProductRepository.findByOrderId(order.getId());
        for (OrderProduct item : orderProductList) {
            if (Objects.equals(item.getProductId(), requestDto.productId())) {
                item.updateOrderProductInfo(requestDto.quantity(), requestDto.price());
                return new UpdateOrderResultDto(true, orderMapper.mapToOrderProductResponseDto(item));
            }
        }
        return null;
    }

    private void handlingOrderFailure(boolean commitStockReservationResult, String transactionId) {

        if (commitStockReservationResult) {
            try {
                orderStockValidator.rollbackCommittedStock(transactionId);
            } catch (Exception ex) {
                log.error("재고 롤백 실패 rollbackCommittedStock() transactionId: {}", transactionId);
            }
        }
    }

    private void createOrderProduct(Order order, OrderCreateRequestDto requestDto) {
        List<OrderProductInfoDto> orderProductInfoDtoList = requestDto.orderProductList();

        for (OrderProductInfoDto item : orderProductInfoDtoList) {
            orderProductRepository.save(orderMapper.mapToOrderProduct(order, item));
        }
    }

    private Order createOrder(OrderCreateRequestDto requestDto, String transactionId) {
        return orderRepository.save(orderMapper.mapToOrder(requestDto, transactionId));
    }

    private void payment() {
        log.info("결제 성공");
//        throw new IllegalStateException("결제 에러");
    }
}
