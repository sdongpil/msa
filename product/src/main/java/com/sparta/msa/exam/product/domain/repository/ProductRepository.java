package com.sparta.msa.exam.product.domain.repository;

import com.sparta.msa.exam.product.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContainingAndDeletedAtIsNull(String name, Pageable pageable);

}
