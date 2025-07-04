package com.example.shop_mall_back.admin.order.dto;

import com.example.shop_mall_back.user.Order.domain.OrderReturn;
import lombok.Getter;

@Getter
public class ClaimManageDto {

    private Long claimId;

    private OrderReturn orderReturn;

    private Boolean isApproved;
}
