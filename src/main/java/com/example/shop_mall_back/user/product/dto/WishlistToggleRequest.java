package com.example.shop_mall_back.user.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishlistToggleRequest {
    private Long userId;
    private Long productId;
}
