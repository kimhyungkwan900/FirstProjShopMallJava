package com.example.shop_mall_back.user.product.dto;

import com.example.shop_mall_back.user.product.domain.Brand;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BrandDto {

    private Long id;
    private String name;

    public static BrandDto from(Brand brand) {
        return BrandDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }
}
