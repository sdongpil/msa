package com.sparta.msa.exam.product.service;

import com.sparta.msa.exam.product.domain.entity.Product;
import com.sparta.msa.exam.product.domain.repository.ProductRepository;
import com.sparta.msa.exam.product.dto.ProductRequestDto;
import com.sparta.msa.exam.product.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final RedisTemplate<String, String> redisTemplate;


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

    @Transactional
    public boolean prepare(Long id, int quantity, String transactionId) {
        log.info("prepare 호출");
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalStateException("상품을 찾을 수 없습니다."));

        log.info("id {}",id);
        log.info("quantity {}", quantity);

        if (product.getStock() < quantity) {
            return false;
        }

//        String reserveKey = "productId" +id+ ":" + transactionId;
//        redisTemplate.opsForValue().set(reserveKey, String.valueOf(quantity));
        String reserveKey = "product:reserve:" + transactionId;
        redisTemplate.opsForHash().put(reserveKey, id.toString(), String.valueOf(quantity));

        return true;
    }

    public void rollbackPrepare(String transactionId) {
        String reserveKey = "product:reserve:" + transactionId;
        redisTemplate.delete(reserveKey);
    }

    @Transactional
    public void commit(String transactionId) {
        String reserveKey = "product:reserve:" + transactionId;

        Map<Object, Object> entries = redisTemplate.opsForHash().entries(reserveKey);
        if (entries.isEmpty()) {
            throw new IllegalStateException("재고 정보를 불러올 수 없습니다.");
        }

        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            Long productId = Long.valueOf(entry.getKey().toString());
            int quantity = Integer.parseInt(entry.getValue().toString());

            Product product = productRepository.findById(productId).orElseThrow();
            product.decrease(quantity);
        }

        redisTemplate.delete(reserveKey);
    }
}
