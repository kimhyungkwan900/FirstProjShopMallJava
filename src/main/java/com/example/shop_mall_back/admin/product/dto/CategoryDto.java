package com.example.shop_mall_back.admin.product.dto;

import com.example.shop_mall_back.user.product.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;

    private String name;

    private Category category;
}
