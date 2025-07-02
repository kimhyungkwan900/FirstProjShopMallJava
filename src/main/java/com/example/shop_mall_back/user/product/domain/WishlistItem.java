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
@Table(name = "wishlist_items")
@IdClass(WishlistItemId.class)
@Builder
@AllArgsConstructor
public class WishlistItem {

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member user;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public void setUser(Member member) {
        this.user = member;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
