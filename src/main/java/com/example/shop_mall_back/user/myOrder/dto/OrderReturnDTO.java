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
    private OrderReturn.ReturnType status;
    private String reason;
    private String detail;
    private LocalDateTime regDte =  LocalDateTime.now();
}
