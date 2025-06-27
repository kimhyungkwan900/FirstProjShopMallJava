package com.example.shop_mall_back.user.Cart.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class WishListDto {
    private Long member_id;
    private Long product_id;
}
