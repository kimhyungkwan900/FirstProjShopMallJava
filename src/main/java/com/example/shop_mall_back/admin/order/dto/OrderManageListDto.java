package com.example.shop_mall_back.admin.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
@AllArgsConstructor
public class OrderManageListDto {
    private Page<OrderManageDto> orders;
    private OrderSearchDto orderSearchDto;
    private Integer maxPage;
}
