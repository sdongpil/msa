package com.sparta.msa.exam.product.controller;

import com.sparta.msa.exam.product.dto.ProductRequestDto;
import com.sparta.msa.exam.product.dto.ProductResponseDto;
import com.sparta.msa.exam.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Value("${server.port}")
    private String port;

    @GetMapping("/product/test")
    public String test() {
        return "test_ 33" + port;
    }

    @PostMapping("/products")
    public ResponseEntity<?> createProducts(@RequestBody ProductRequestDto requestDto) {
        ProductResponseDto responseDto = productService.create(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProduct() {
        List<ProductResponseDto> responseDto = productService.getProducts();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @GetMapping("/products/{id}/prepare")
    public boolean prepare(@PathVariable Long id, @RequestParam int quantity, @RequestParam String transactionId) {
        return productService.prepare(id, quantity, transactionId);
    }

    @PostMapping("/products/prepare/rollback")
    public void rollback(String transactionId){
        productService.rollbackPrepare(transactionId);
    }

    @PostMapping("/products/commit")
    public void commit(@RequestParam String transactionId) {
        productService.commit(transactionId);

    }
}
