package com.example.shop_mall_back.user.product.domain;

import com.example.shop_mall_back.common.domain.Member;
import com.example.shop_mall_back.common.domain.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@IdClass(WishlistItemId.class)
public class WishlistItem {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member user;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
