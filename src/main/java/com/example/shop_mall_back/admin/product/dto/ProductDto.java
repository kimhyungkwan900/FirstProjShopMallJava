package com.example.shop_mall_back.admin.product.dto;

import com.example.shop_mall_back.common.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
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

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    //외래키
    private BrandDto brand;

    private CategoryDto category;
}
