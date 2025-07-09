package com.example.shop_mall_back.user.myOrder.dto;


import com.example.shop_mall_back.user.myOrder.domain.OrderReturn;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderReturnDTO {
    private Long id;
    private Long memberId;
    private Long orderId;
    private OrderReturn.ReturnType returnType;
    private String reason;
    private String detail;
    private LocalDateTime regDate =  LocalDateTime.now();
}
