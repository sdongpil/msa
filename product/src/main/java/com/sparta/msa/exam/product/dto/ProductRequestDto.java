package com.sparta.msa.exam.product.dto;

import com.sparta.msa.exam.product.domain.ProductStatus;
import com.sparta.msa.exam.product.domain.entity.Product;

public record ProductRequestDto(
        String name,
        int supplyPrice,
        int stock,
        ProductStatus productStatus
) {

}
