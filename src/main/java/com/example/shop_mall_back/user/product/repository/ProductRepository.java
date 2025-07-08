package com.example.shop_mall_back.user.product.repository;

import com.example.shop_mall_back.common.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    // JpaRepository<Product, Long> → Product 엔티티에 대해 기본 CRUD + 페이징 + 정렬 제공
    // JpaSpecificationExecutor<Product> → 동적 조건 검색을 위한 지원 인터페이스

    // 상품 이름에 특정 키워드가 포함된 상품들을 페이징 처리하여 검색
    Page<Product> findByNameContaining(String keyword, Pageable pageable);

    // 특정 브랜드 ID에 속한 상품들을 페이징 처리하여 검색
    Page<Product> findByBrandId(Long brandId, Pageable pageable);

    // 특정 카테고리 ID에 속한 상품들을 페이징 처리하여 검색
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    // 여러 카테고리 ID에 속한 상품들을 페이징 처리하여 검색 (하위 카테고리 포함 등)
    Page<Product> findByCategoryIdIn(List<Long> categoryIds, Pageable pageable);
}
