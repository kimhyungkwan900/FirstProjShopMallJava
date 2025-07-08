package com.example.shop_mall_back.user.product.domain;

import com.example.shop_mall_back.common.domain.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // 이 클래스가 JPA의 엔티티임을 선언
@Getter // 모든 필드에 대한 getter 메서드 자동 생성
@Table(name = "product_category") // 이 엔티티가 매핑될 테이블 이름을 "product_category"로 지정
@NoArgsConstructor // 파라미터가 없는 기본 생성자 자동 생성 (JPA 필수)
@IdClass(ProductCategoryId.class) // 복합 기본 키(Composite Key)를 정의할 클래스 지정
public class ProductCategory {

    @Id // 복합 키의 일부로 지정
    @ManyToOne // 다대일 관계: 여러 ProductCategory → 하나의 Product
    @JoinColumn(name = "product_id") // product_id 컬럼이 외래 키(FK)가 됨
    private Product product; // 연결된 상품 정보

    @Id // 복합 키의 일부로 지정
    @ManyToOne // 다대일 관계: 여러 ProductCategory → 하나의 Category
    @JoinColumn(name = "category_id") // category_id 컬럼이 외래 키(FK)가 됨
    private Category category; // 연결된 카테고리 정보
}
