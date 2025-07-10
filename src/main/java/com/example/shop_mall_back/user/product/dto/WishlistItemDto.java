package com.example.shop_mall_back.user.product.dto;

import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.product.domain.WishlistItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WishlistItemDto {
    private Long productId;
    private String productName;
    private String brandName;
    private int price;
    private List<ProductImageDto> images;
    private String sellStatus;

    public static WishlistItemDto from(WishlistItem item) {
        Product product = item.getProduct();

        return WishlistItemDto.builder()
                .productId(product.getId())
                .productName(product.getName())
                .brandName(product.getBrand().getName())
                .price(product.getPrice())
                .images(product.getImages().stream().map(ProductImageDto::from).toList())
                .sellStatus(product.getSellStatus().name())
                .build();
    }
}
