package com.sparta.msa.exam.order.service.validator;

import com.sparta.msa.exam.order.dto.order.OrderCreateRequestDto;
import com.sparta.msa.exam.order.dto.order.OrderUpdateRequestDto;

public interface OrderStockValidator {
    boolean commit(String transactionId);

    void reserveProductStocks(OrderCreateRequestDto requestDto, String transactionId);

    void reserveProductStocks(OrderUpdateRequestDto requestDto, String transactionId);

    void rollbackCommittedStock(String transactionId);

    void deleteStockReservation(String transactionId);
}
