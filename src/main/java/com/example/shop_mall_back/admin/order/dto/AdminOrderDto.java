package com.example.shop_mall_back.admin.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminOrderDto {
    private Long id;    //주문 id
    private String member_id; //주문자 회원 id
    private Long delivery_address_id; //배송지 id
    private String delivery_address;
    private LocalDateTime order_date;   //주문 일시
    private int total_amount;   //총 주문 결제 금액
    private int total_count;    //총 주문 개수
    private String payment_method;  //결제 수단
    private String delivery_request;    //배송 요청 사항
}