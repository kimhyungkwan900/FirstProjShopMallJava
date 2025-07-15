package com.example.shop_mall_back.user.Order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {

    private Long id;              // 주문 항목 ID
    private Long orderId;         // 주문 ID
    private Long productId;       // 상품 ID
    private int quantity;         // 수량
    private int price;            // 가격
    private String brandName;   //브랜드 이름
    private String imageUrl;    //상품 이미지
    private String productTitle;    //상품 이름
    private String productPrice;    //상품 가격


}