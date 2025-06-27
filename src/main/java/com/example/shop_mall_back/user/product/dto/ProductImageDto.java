package com.example.shop_mall_back.user.product.dto;

import com.example.shop_mall_back.user.product.domain.ProductImage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductImageDto {
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private boolean isRepImg;

    public static ProductImageDto from(ProductImage image) {
        return ProductImageDto.builder()
                .id(image.getId())
                .imgName(image.getImgName())
                .oriImgName(image.getOriImgName())
                .imgUrl(image.getImgUrl())
                .isRepImg(image.isRepImg())
                .build();
    }
}