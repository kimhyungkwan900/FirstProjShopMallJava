package com.example.shop_mall_back.user.product.dto;

import com.example.shop_mall_back.common.domain.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private int price;
    private int stock;
    private String brandName;
    private int viewCount;
    private String sellStatus;
    private String deliveryInfo;
    private String categoryName;
    private List<ProductImageDto> images;

    public static ProductDto from(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .brandName(product.getBrand().getName())
                .viewCount(product.getViewCount())
                .sellStatus(product.getSellStatus().name())
                .deliveryInfo(product.getDeliveryInfo().getDeliveryCom().name())
                .categoryName(product.getCategory().getName())
                .images(
                        product.getImages().stream()
                                .map(ProductImageDto::from)
                                .collect(Collectors.toList())
                )
                .build();
    }
}

