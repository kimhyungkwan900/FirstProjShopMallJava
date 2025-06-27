package com.example.shop_mall_back.user.product.repository;

import com.example.shop_mall_back.user.product.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentIsNull(); // 최상위 카테고리 조회용
    List<Category> findByParentId(Long parentId); // 특정 상위 카테고리의 하위 카테고리들
}
