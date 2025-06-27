package com.example.shop_mall_back.user.product.repository;
import com.example.shop_mall_back.user.product.domain.ProductCategory;
import com.example.shop_mall_back.user.product.domain.ProductCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {
}
