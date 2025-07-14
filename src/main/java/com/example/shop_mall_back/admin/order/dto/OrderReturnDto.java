package com.example.shop_mall_back.admin.order.dto;

import com.example.shop_mall_back.user.myOrder.domain.OrderReturn;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderReturnDto {
    private Long id;
    private Long orderId;
    private Long memberId;
    private OrderReturn.ReturnType returnType;
    private String reason;
    private String detail;
    private LocalDateTime regDate;
}
