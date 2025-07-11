package com.example.shop_mall_back.user.myOrder.dto;

import com.example.shop_mall_back.user.myOrder.domain.OrderReturn;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderChangeHistoryDTO {
    private Long id;

    private Long memberId;

    private Long orderId;

    private OrderProductDTO product;

    private Integer totalAmount;

    private Integer totalCount;

    private LocalDateTime orderDate;

    private OrderReturn.ReturnType returnType;

    private String reason;

    private String detail;

    private LocalDateTime regDate;

    private boolean orderDelete;
}
