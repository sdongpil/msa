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

    @PostMapping("/stock/reservation/{transactionId}/rollback")
    public void rollbackStockReservation(@PathVariable String transactionId){
        productInventoryService.rollbackStockReservation(transactionId);
    }

    @PostMapping("/stock/reservation/{transactionId}/commit")
    public void commitStockReservation(@RequestParam String transactionId) {
        productInventoryService.commitStockReservation(transactionId);

    }
}
