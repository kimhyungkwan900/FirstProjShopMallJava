package com.example.shop_mall_back.user.Cart.dto;

import com.example.shop_mall_back.user.Cart.domain.RestockAlarm;
import com.example.shop_mall_back.user.product.domain.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestockAlarmDto {
    private Long productId;  //상품 Id
    private String imageUrl;    //상품 이미지
    private String productName; //상품 이름
    private String productBrandName;    //상품 브랜드 이름

}
