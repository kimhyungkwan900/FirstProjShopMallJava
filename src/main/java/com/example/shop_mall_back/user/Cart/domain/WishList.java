package com.example.shop_mall_back.user.Cart.domain;

import com.example.shop_mall_back.common.domain.Member;
import com.example.shop_mall_back.common.domain.Product;
import jakarta.persistence.*;

@Entity
@IdClass(WishListId.class)
public class WishList {
    @Id
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
