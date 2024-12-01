package com.sparta.msa.exam.order.service;

import com.sparta.msa.exam.order.domain.entity.OrderStatus;
import com.sparta.msa.exam.order.domain.entity.order.Order;
import com.sparta.msa.exam.order.domain.entity.order.OrderProduct;
import com.sparta.msa.exam.order.domain.repository.OrderProductRepository;
import com.sparta.msa.exam.order.domain.repository.OrderRepository;
import com.sparta.msa.exam.order.dto.OrderProductInfoDto;
import com.sparta.msa.exam.order.dto.OrderRequestDto;
import com.sparta.msa.exam.order.dto.StockReservationRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStockValidatorImpl implements OrderStockValidator{
    private final ProductClient productClient;
    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;


    public void commit(String transactionId) {
        productClient.commit(transactionId);
    }

    public Order orderInfoSave(OrderRequestDto requestDto) {
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

        return order;
    }

    public void reserveProductStocks(OrderRequestDto requestDto, String transactionId) {
        Map<Long, Boolean> prepareResults = new HashMap<>();

        List<OrderProductInfoDto> orderProductInfoDtoList = requestDto.orderProductList();

        for (OrderProductInfoDto item : orderProductInfoDtoList) {
            boolean prepareStock = productClient.prepareStockReservation(item.getProductId(), new StockReservationRequestDto(item.getQuantity(), transactionId));

            prepareResults.put(item.getProductId(), prepareStock);

            if (!prepareStock) {
                rollback(transactionId, prepareResults);
                throw new IllegalStateException("재고 문제 발생");
            }
        }
    }

    public void rollback(String transactionId, Map<Long, Boolean> prepareResults) {
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

}
