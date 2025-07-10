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
@AllArgsConstructor
@Builder
@Table(name = "wishlist_items")
public class WishlistItem {

    @EmbeddedId
    private WishlistItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")  // WishlistItemId.memberId와 매핑
    @JoinColumn(name = "member_id")
    private Member user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")  // WishlistItemId.productId와 매핑
    @JoinColumn(name = "product_id")
    private Product product;

    public static WishlistItem of(Member member, Product product) {
        return WishlistItem.builder()
                .id(new WishlistItemId(member.getId(), product.getId()))
                .user(member)
                .product(product)
                .build();
    }

    // 명시적으로 사용자 설정 가능하도록 set 메서드 정의
    public void setUser(Member member) {
        this.user = member;
    }

    // 명시적으로 상품 설정 가능하도록 set 메서드 정의
    public void setProduct(Product product) {
        this.product = product;
    }
}

