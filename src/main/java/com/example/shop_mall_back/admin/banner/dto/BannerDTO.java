package com.example.shop_mall_back.admin.banner.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BannerDTO {
    private Long id;
    private String imageUrl;
    private String link;
    private String alt;
}
