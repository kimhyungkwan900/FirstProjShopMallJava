package com.example.shop_mall_back.admin.product;

import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.product.domain.Brand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;

    private String name;

    private String description;

    private int price;

    private int stock;

    private int viewCount;

    private Product.SellStatus sellStatus;

    //외래키
    private Long brand_id;

    private Long category_id;
}
