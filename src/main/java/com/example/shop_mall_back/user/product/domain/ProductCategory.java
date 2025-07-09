package com.example.shop_mall_back.user.product.domain;

import com.example.shop_mall_back.common.domain.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "product_category") // 이 엔티티가 매핑될 테이블 이름을 "product_category"로 지정
@NoArgsConstructor
@IdClass(ProductCategoryId.class)
public class ProductCategory {

    @Id
    @ManyToOne // 다대일 관계: 여러 ProductCategory → 하나의 Product
    @JoinColumn(name = "product_id") // product_id 컬럼이 외래 키(FK)가 됨
    private Product product; // 연결된 상품 정보

    @Id
    @ManyToOne // 다대일 관계: 여러 ProductCategory → 하나의 Category
    @JoinColumn(name = "category_id") // category_id 컬럼이 외래 키(FK)가 됨
    private Category category; // 연결된 카테고리 정보
}
