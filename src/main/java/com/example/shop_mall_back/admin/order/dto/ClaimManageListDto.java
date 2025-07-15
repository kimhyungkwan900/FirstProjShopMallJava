package com.example.shop_mall_back.admin.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
@AllArgsConstructor
public class ClaimManageListDto {
    private Page<ClaimManageDto> claims;
    private ClaimSearchDto claimSearchDto;
    private Integer maxPage;
    private Integer totalPage;
}
