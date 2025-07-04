package com.example.shop_mall_back.admin.order.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderSearchDto {

    //검색조건 선택: 주문ID, 주문자ID, 주문 상태, 주문일자(regtime)

    private String searchType;  //orderId, name, memberId

    private String searchContent;

    private String orderStatus;

    private String dateType;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
