package com.example.shop_mall_back.user.product.domain;

import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "wishlist_items") // 이 엔티티가 매핑될 테이블 이름을 "wishlist_items"로 지정
@IdClass(WishlistItemId.class)
@Builder
@AllArgsConstructor
public class WishlistItem {

    @Id
    @ManyToOne // 회원과의 다대일 관계 (여러 WishlistItem → 한 Member)
    @JoinColumn(name = "member_id") // 외래 키 컬럼명 지정 (회원 ID)
    private Member user; // 찜한 사용자의 정보

    @Id
    @ManyToOne // 상품과의 다대일 관계 (여러 WishlistItem → 한 Product)
    @JoinColumn(name = "product_id") // 외래 키 컬럼명 지정 (상품 ID)
    private Product product; // 찜한 상품의 정보

    // 명시적으로 사용자 설정 가능하도록 set 메서드 정의
    public void setUser(Member member) {
        this.user = member;
    }

    // 명시적으로 상품 설정 가능하도록 set 메서드 정의
    public void setProduct(Product product) {
        this.product = product;
    }
}

