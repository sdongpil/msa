package com.sparta.msa.exam.order.service;

import com.sparta.msa.exam.order.domain.entity.OrderStatus;
import com.sparta.msa.exam.order.domain.entity.order.Order;
import com.sparta.msa.exam.order.domain.repository.OrderProductRepository;
import com.sparta.msa.exam.order.domain.repository.OrderRepository;
import com.sparta.msa.exam.order.dto.CommitResponseDto;
import com.sparta.msa.exam.order.dto.order.OrderCreateRequestDto;
import com.sparta.msa.exam.order.dto.orderProduct.OrderProductInfoDto;
import com.sparta.msa.exam.order.exception.ErrorCode;
import com.sparta.msa.exam.order.exception.OrderException;
import com.sparta.msa.exam.order.service.mapper.OrderMapper;
import com.sparta.msa.exam.order.service.validator.OrderStockValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionCoordinator {
    private final OrderStockValidator orderStockValidator;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderProductRepository orderProductRepository;

    public void prepare(OrderCreateRequestDto requestDto, String transactionId) {
        //재고 수량 확인 준비
        orderStockValidator.reserveProductStocks(requestDto, transactionId);
    }

    @Transactional
    public CommitResponseDto commit(OrderCreateRequestDto requestDto, String transactionId) {
        // 주문 저장
        Order order = createOrder(requestDto, transactionId);

        // 주문 상품 저장
        createOrderProduct(order, requestDto);

        // 재고 차감 커밋
        boolean commitStockReservationResult = orderStockValidator.commit(transactionId);

        order.setStatus(OrderStatus.SUCCESS);

        return new CommitResponseDto(order, commitStockReservationResult);
    }

    public void rollback(boolean commitStockReservationResult, String transactionId) {
        if (commitStockReservationResult) {
            Order order = orderRepository.findByTransactionId(transactionId).orElseThrow(() -> new OrderException(ErrorCode.ORDER_NOT_FOUND));
            if (order != null) {
                order.setStatus(OrderStatus.FAIL);
            }
        }

        handlingOrderFailure(commitStockReservationResult, transactionId);
    }

    public void rollbackV2(boolean commitStockReservationResult, String transactionId) {
        handlingOrderFailure(commitStockReservationResult, transactionId);
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
//        log.info("결제 성공");
//        throw new IllegalStateException("결제 에러");
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


}
