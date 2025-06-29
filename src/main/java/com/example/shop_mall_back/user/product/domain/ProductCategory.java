package com.example.shop_mall_back.user.product.domain;

import com.example.shop_mall_back.common.domain.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "product_category")
@NoArgsConstructor
@IdClass(ProductCategoryId.class)
public class ProductCategory {

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Id
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}