package com.example.shop_mall_back.admin.order.dto;

import com.example.shop_mall_back.user.myOrder.domain.OrderReturn;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ClaimSearchDto {

    //검색조건 선택: 주문ID, 고객ID, 고객 요청 유형, 요청일자(regtime)

    private String searchType;  //orderId, memberId

    private String searchContent;

    private OrderReturn.ReturnType returnType;

    private String dateType;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
