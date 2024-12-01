package com.sparta.msa.exam.product.service;

import com.sparta.msa.exam.product.domain.entity.Product;
import com.sparta.msa.exam.product.domain.repository.ProductRepository;
import com.sparta.msa.exam.product.dto.ProductRequestDto;
import com.sparta.msa.exam.product.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponseDto create(ProductRequestDto requestDto) {
        Product product = Product.toEntity(requestDto);

        Product save = productRepository.save(product);

        return ProductResponseDto.from(save);
    }

    public List<ProductResponseDto> getProducts() {

        return productRepository.findAll()
                .stream()
                .map(ProductResponseDto::from)
                .toList();
    }
}
