package com.sparta.msa.exam.product.controller;

import com.sparta.msa.exam.product.dto.StockReservationRequestDto;
import com.sparta.msa.exam.product.service.ProductInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductInventoryController {
    private final ProductInventoryService productInventoryService;

    @PostMapping("/{id}/stock/reservation")
    public boolean prepareStockReservation(@PathVariable Long id, @RequestBody StockReservationRequestDto requestDto) {
        return productInventoryService.prepareStockReservation(id, requestDto);
    }

    @PostMapping("/stock/reservation/{transactionId}/commit")
    public boolean commitStockReservation(@PathVariable String transactionId) {
        return productInventoryService.commitStockReservation(transactionId);
    }

    @PostMapping("/stock/commit/{transactionId}/rollback")
    public void rollbackCommittedStock(@PathVariable String transactionId) {
        productInventoryService.rollbackCommittedStock(transactionId);
    }

    @DeleteMapping("/stock/reservation/{transactionId}")
    public void redisDelete(@PathVariable String transactionId) {
        productInventoryService.deleteStockReservation(transactionId);
    }
}
