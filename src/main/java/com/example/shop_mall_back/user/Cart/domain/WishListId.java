package com.example.shop_mall_back.user.Cart.domain;

import jakarta.persistence.IdClass;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class WishListId implements Serializable {
    private Long member;
    private Long product;
}
