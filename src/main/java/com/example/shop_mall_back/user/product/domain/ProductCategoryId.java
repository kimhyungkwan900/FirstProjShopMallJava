package com.example.shop_mall_back.user.product.domain;

import java.io.Serializable;
import java.util.Objects;

public class ProductCategoryId implements Serializable {

    private Long product;
    private Long category;

    public ProductCategoryId() {}

    public ProductCategoryId(Long product, Long category) {
        this.product = product;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategoryId that = (ProductCategoryId) o;
        return Objects.equals(product, that.product) && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, category);
    }
}
