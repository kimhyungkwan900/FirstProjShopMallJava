package com.example.shop_mall_back.user.product.domain;

import com.example.shop_mall_back.common.domain.Member;
import com.example.shop_mall_back.common.domain.Product;

import java.io.Serializable;
import java.util.Objects;

public class WishlistItemId implements Serializable {

    private Long user;
    private Long product;

    public WishlistItemId() {}

    public WishlistItemId(Long user, Long product) {
        this.user = user;
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishlistItemId that = (WishlistItemId) o;
        return Objects.equals(user, that.user) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, product);
    }
}
