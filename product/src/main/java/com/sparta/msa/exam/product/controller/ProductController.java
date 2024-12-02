package com.sparta.msa.exam.product.controller;

import com.sparta.msa.exam.product.dto.ProductRequestDto;
import com.sparta.msa.exam.product.dto.ProductResponseDto;
import com.sparta.msa.exam.product.service.ProductInventoryService;
import com.sparta.msa.exam.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductInventoryService productInventoryService;
    private final ProductService productService;

    @PostMapping()
    public ResponseEntity<?> createProducts(@RequestBody ProductRequestDto requestDto) {
        ProductResponseDto responseDto = productService.create(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping()
    public ResponseEntity<?> getProduct(Pageable pageable) {
        Page<ProductResponseDto> responseDto = productService.getProducts(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}
