package com.sparta.msa.exam.order.service;

import com.sparta.msa.exam.order.dto.StockReservationRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product")
public interface ProductClient {
    @PostMapping("/products/{id}/stock/reservation")
    boolean prepareStockReservation(@PathVariable Long id, @RequestBody StockReservationRequestDto requestDto);

    @PostMapping("/products/stock/reservation/{transactionId}/rollback")
    void rollbackStockReservation(@PathVariable String transactionId);

    @PostMapping("/products/stock/reservation/{transactionId}/commit")
    boolean commitStockReservation(@PathVariable String transactionId);

    @PostMapping("/products/stock/commit/{transactionId}/rollback")
    void rollbackCommittedStock(@PathVariable String transactionId);

    @DeleteMapping("/products/stock/reservation/{transactionId}")
    void deleteStockReservation(@PathVariable String transactionId);
}

