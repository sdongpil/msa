package com.sparta.msa.exam.product.domain.entity;

import com.sparta.msa.exam.product.domain.ProductStatus;
import com.sparta.msa.exam.product.dto.ProductRequestDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Builder
@Slf4j
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private int supplyPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    public static Product toEntity(ProductRequestDto requestDto) {
        return Product.builder()
                .name(requestDto.name())
                .supplyPrice(requestDto.supplyPrice())
                .stock(requestDto.stock())
                .productStatus(requestDto.productStatus())
                .build();
    }

    public void decrease(int quantity) {
        log.info("decrease 호출");
        this.stock -= quantity;
    }
}
