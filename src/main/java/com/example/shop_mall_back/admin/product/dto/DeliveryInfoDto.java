package com.example.shop_mall_back.admin.product.dto;

import com.example.shop_mall_back.admin.product.domain.DeliveryInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DeliveryInfoDto {
    private Long id;

    private String delivery_yn;

    private DeliveryInfo.Delivery_com deliveryCom;

    private int deliveryPrice;
}
