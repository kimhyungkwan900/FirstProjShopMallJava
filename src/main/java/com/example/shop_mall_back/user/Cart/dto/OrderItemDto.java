package com.example.shop_mall_back.user.Cart.dto;

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
    private String selected_option; // 선택 옵션

}
