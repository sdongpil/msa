package com.sparta.msa.exam.product.service;

import com.sparta.msa.exam.product.domain.entity.Product;
import com.sparta.msa.exam.product.domain.repository.ProductRepository;
import com.sparta.msa.exam.product.dto.ProductRequestDto;
import com.sparta.msa.exam.product.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    @CacheEvict(cacheNames = "productsCacheAll", allEntries = true)
    public ProductResponseDto create(ProductRequestDto requestDto) {
        Product product = Product.toEntity(requestDto);

        Product save = productRepository.save(product);

        return ProductResponseDto.from(save);
    }

    @Cacheable(cacheNames = "productsCacheAll", key = "'product:' + ':page:' + #pageable.pageNumber + ':size:' + #pageable.pageSize + ':sort:' + #pageable.sort")
    public Page<ProductResponseDto> getProducts(Pageable pageable) {

        return productRepository.findAll(pageable).map(ProductResponseDto::from);
    }
}
