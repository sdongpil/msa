package com.sparta.msa.exam.product.domain;

public enum ProductStatus {
    AVAILABLE("판매 가능"),
    SOLD_OUT("품절"),
    ON_HOLD("판매 중지");


    private final String description;

    ProductStatus(String description) {
        this.description = description;
    }
}
