package com.sparta.msa.exam.product.service;

import com.sparta.msa.exam.product.domain.entity.Product;
import com.sparta.msa.exam.product.dto.ProductRequestDto;
import com.sparta.msa.exam.product.dto.ProductResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product mapToEntity(ProductRequestDto requestDto) {
        return Product.builder()
                .name(requestDto.name())
                .supplyPrice(requestDto.supplyPrice())
                .stock(requestDto.stock())
                .productStatus(requestDto.productStatus())
                .build();
    }

    public ProductResponseDto mapToProductResponseDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getSupplyPrice(),
                product.getProductStatus(),
                product.getStock(),
                product.getCreatedAt()
        );
    }

}
