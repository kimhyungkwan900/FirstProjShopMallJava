package com.example.shop_mall_back.admin.product;

import com.example.shop_mall_back.common.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ProductRepository extends JpaRepository<Product,Long> {


}
