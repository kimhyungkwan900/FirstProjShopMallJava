package com.example.shop_mall_back.admin.order.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimManageDto {

    private Long claimId;

    private OrderReturnDto orderReturn;

    private Boolean isApproved;
}
