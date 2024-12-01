package com.sparta.msa.exam.order.service;

import com.sparta.msa.exam.order.dto.ProductResponseDto;
import com.sparta.msa.exam.order.dto.StockReservationRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product")
public interface ProductClient {

    @GetMapping("/product/test")
    String test();

    @GetMapping("/products")
    List<ProductResponseDto> getProductList();

    @GetMapping("/products/{id}")
    boolean stockCheck(@PathVariable Long id, @RequestParam int quantity);





    @PostMapping("/products/{id}/stock/reservation")
    boolean prepareStockReservation(@PathVariable Long id, @RequestBody StockReservationRequestDto requestDto);

    @PostMapping("/products/stock/reservation/{transactionId}/rollback")
    void rollbackStockReservation(@PathVariable String transactionId);

    @PostMapping("/products/stock/reservation/{transactionId}/commit")
    void commitStockReservation(@PathVariable String transactionId);
}

