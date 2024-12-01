package com.sparta.msa.exam.order.service;

import com.sparta.msa.exam.order.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "product")
public interface ProductClient {

    @GetMapping("/product/test")
    String test();

    @GetMapping("/products")
    List<ProductResponseDto> getProductList();
}
