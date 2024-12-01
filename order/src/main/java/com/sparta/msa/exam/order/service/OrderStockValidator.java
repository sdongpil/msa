package com.sparta.msa.exam.order.service;

import com.sparta.msa.exam.order.domain.entity.order.Order;
import com.sparta.msa.exam.order.dto.OrderRequestDto;

import java.util.Map;

public interface OrderStockValidator {
    void commit(String transactionId);

    Order orderInfoSave(OrderRequestDto requestDto);

    void reserveProductStocks(OrderRequestDto requestDto, String transactionId);

    void rollback(String transactionId, Map<Long, Boolean> prepareResults);
}
