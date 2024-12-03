package com.sparta.msa.exam.order.service.validator;

import com.sparta.msa.exam.order.dto.OrderRequestDto;

public interface OrderStockValidator {
    boolean commit(String transactionId);

    void reserveProductStocks(OrderRequestDto requestDto, String transactionId);

    void rollbackCommittedStock(String transactionId);

    void deleteStockReservation(String transactionId);
}
