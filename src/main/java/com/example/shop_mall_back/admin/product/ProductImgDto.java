package com.example.shop_mall_back.admin.product;

import com.example.shop_mall_back.common.domain.ProductImg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.modelmapper.ModelMapper;

@Getter
@AllArgsConstructor
@Builder
public class ProductImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    //ProductImg 객체를 받아 ProductImgDto 객체로 변환해주는 정적 메소드
    public static ProductImgDto of(ProductImg productImg) {
         return modelMapper.map(productImg, ProductImgDto.class);
    }
}
