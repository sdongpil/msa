package com.sparta.msa.exam.order.service;

import com.sparta.msa.exam.order.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product")
public interface ProductClient {

    @GetMapping("/product/test")
    String test();

    @GetMapping("/products")
    List<ProductResponseDto> getProductList();

    @GetMapping("/products/{id}")
    boolean stockCheck(@PathVariable Long id, @RequestParam int quantity);





    @GetMapping("/products/{id}/prepare")
    boolean prepareStock(@PathVariable Long id, @RequestParam int quantity, @RequestParam String transactionId);

    @PostMapping("/products/prepare/rollback")
    void rollbackPrepare(@RequestParam String transactionId);

    @PostMapping("/products/commit")
    void commit(@RequestParam String transactionId);
}
