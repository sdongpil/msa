package com.sparta.msa.exam.order.service.validator;

import com.sparta.msa.exam.order.domain.entity.OrderStatus;
import com.sparta.msa.exam.order.domain.entity.order.Order;
import com.sparta.msa.exam.order.domain.repository.OrderRepository;
import com.sparta.msa.exam.order.dto.OrderProductInfoDto;
import com.sparta.msa.exam.order.dto.OrderRequestDto;
import com.sparta.msa.exam.order.dto.StockReservationRequestDto;
import com.sparta.msa.exam.order.service.ProductClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStockValidatorImplV1 implements OrderStockValidator {
    private final ProductClient productClient;
    private final OrderRepository orderRepository;

    @Override
    public boolean commit(String transactionId) {
        boolean result = productClient.commitStockReservation(transactionId);

        Order order = orderRepository.findByTransactionId(transactionId);
        order.setStatus(OrderStatus.SUCCESS);
        return result;
    }

    @Override
    public void reserveProductStocks(OrderRequestDto requestDto, String transactionId) {
        List<OrderProductInfoDto> orderProductInfoDtoList = requestDto.orderProductList();
        for (OrderProductInfoDto item : orderProductInfoDtoList) {
            boolean prepareStock = productClient.prepareStockReservation(item.getProductId(), new StockReservationRequestDto(item.getQuantity(), transactionId));

            if (!prepareStock) {
                throw new IllegalStateException("재고 문제 발생");
            }
        }
    }

    @Override
    @Transactional
    public void rollbackCommittedStock(String transactionId) {
        productClient.rollbackCommittedStock(transactionId);

        Order order = orderRepository.findByTransactionId(transactionId);
        order.setStatus(OrderStatus.FAIL);
    }

    @Override
    public void deleteStockReservation(String transactionId) {
        productClient.deleteStockReservation(transactionId);
    }
}
