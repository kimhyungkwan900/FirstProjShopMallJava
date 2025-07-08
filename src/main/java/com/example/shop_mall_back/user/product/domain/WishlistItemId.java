package com.example.shop_mall_back.user.product.domain;

import java.io.Serializable;
import java.util.Objects;

public class WishlistItemId implements Serializable {
    // 복합 키(Composite Key)를 정의하는 클래스
    // JPA에서 @IdClass에 사용되며 반드시 Serializable을 구현해야 함

    private Long user;   // WishlistItem의 user 필드와 매핑되는 ID (Member의 PK)
    private Long product; // WishlistItem의 product 필드와 매핑되는 ID (Product의 PK)

    // 기본 생성자 (JPA가 리플렉션으로 인스턴스를 생성할 때 필수)
    public WishlistItemId() {}

    // 모든 필드를 초기화하는 생성자
    public WishlistItemId(Long user, Long product) {
        this.user = user;
        this.product = product;
    }

    // equals() 메서드 오버라이드
    // 두 WishlistItemId 객체가 같은지를 비교 (user와 product가 모두 같을 때 true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 자기 자신과 비교 시 true
        if (o == null || getClass() != o.getClass()) return false; // null이거나 타입이 다르면 false
        WishlistItemId that = (WishlistItemId) o;
        return java.util.Objects.equals(user, that.user) &&
                java.util.Objects.equals(product, that.product); // 두 필드 값이 모두 같으면 true
    }

    // hashCode() 메서드 오버라이드
    // 해시 기반 컬렉션(Set, Map 등)에서 객체 비교를 위해 필수
    // user와 product 값을 조합해 해시코드 생성
    @Override
    public int hashCode() {
        return java.util.Objects.hash(user, product);
    }
}

