package com.example.shop_mall_back.user.product.dto;

import com.example.shop_mall_back.user.product.domain.WishlistItem;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WishlistItemDto {
    private Long productId;
    private String productName;

    public static WishlistItemDto from(WishlistItem item) {
        return WishlistItemDto.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .build();
    }
}
