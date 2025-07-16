package com.example.shop_mall_back.admin.order.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimUpdateDto {
    private Long id;
    private String approval;
}
