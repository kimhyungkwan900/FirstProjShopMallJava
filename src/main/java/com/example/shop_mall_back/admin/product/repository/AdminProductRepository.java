package com.example.shop_mall_back.admin.product.repository;

import com.example.shop_mall_back.common.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProductRepository extends JpaRepository<Product,Long>, AdminProductRepositoryCustom {

    //상품번호 또는 그룹상품번호, 상품명, 제조사명, 브랜드명, 판매상태, 카테고리, 등록일자

}