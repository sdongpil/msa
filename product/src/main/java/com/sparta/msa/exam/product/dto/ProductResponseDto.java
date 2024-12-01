package com.sparta.msa.exam.product.dto;

import com.sparta.msa.exam.product.domain.ProductStatus;
import com.sparta.msa.exam.product.domain.entity.Product;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProductResponseDto(Long id,
                                 String name,
                                 int supplyPrice,
                                 ProductStatus productStatus,
                                 int stock,
                                 LocalDateTime createdAt
) {

    public static ProductResponseDto from(Product product) {
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

