package com.example.shop_mall_back.admin.product.repository;

import com.example.shop_mall_back.admin.product.dto.ProductSearchDto;
import com.example.shop_mall_back.common.domain.Product;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class AdminProductRepositoryCustomImpl implements AdminProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> getProductPageByCondition(ProductSearchDto productSearchDto, Pageable pageable) {
        return null;
    }
}
