package com.example.shop_mall_back.user.Order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSummaryDto {

    private Long orderId;                 // 주문 ID
    private String memberName;            // 주문자 이름
    private String deliveryAddress;       // 배송지
    private String paymentMethod;         // 결제 방식
    private int totalAmount;              // 총액
    private int deliveryFee;              // 배송비
    private LocalDateTime orderDate;      // 주문 일자

    private List<OrderItemDto> orderItems; // 주문 상품 리스트
}
