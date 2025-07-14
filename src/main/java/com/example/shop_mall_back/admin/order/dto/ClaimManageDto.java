package com.example.shop_mall_back.admin.order.dto;

import com.example.shop_mall_back.user.myOrder.domain.OrderReturn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimManageDto {

    private Long claimId;

    private OrderReturn orderReturn;

    private Boolean isApproved;
}
