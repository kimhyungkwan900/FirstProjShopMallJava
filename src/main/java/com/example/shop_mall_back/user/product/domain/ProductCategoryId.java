package com.example.shop_mall_back.user.product.domain;

import java.io.Serializable;
import java.util.Objects;

public class ProductCategoryId implements Serializable {

    // 복합 키를 구성하는 첫 번째 필드 (Product 엔티티의 기본 키와 대응)
    private Long product;

    // 복합 키를 구성하는 두 번째 필드 (Category 엔티티의 기본 키와 대응)
    private Long category;

    public ProductCategoryId() {}

    public ProductCategoryId(Long product, Long category) {
        this.product = product;
        this.category = category;
    }

    // 두 객체가 동일한지 비교하기 위한 equals() 메서드 오버라이드
    // product와 category가 같으면 동일한 복합 키로 간주
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 자기 자신과 비교 시 true
        if (o == null || getClass() != o.getClass()) return false;// null 또는 클래스 타입이 다르면 false
        ProductCategoryId that = (ProductCategoryId) o;// 캐스팅 후 비교
        return Objects.equals(product, that.product) && Objects.equals(category, that.category);// 두 필드가 모두 같을 때 true
    }

    // 해시 기반 컬렉션(Map, Set 등)에서 사용되도록 hashCode 오버라이드
    // product와 category 값을 기반으로 해시 코드 생성
    @Override
    public int hashCode() {
        return Objects.hash(product, category);
    }
}
