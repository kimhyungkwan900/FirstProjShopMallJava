package com.example.shop_mall_back.admin.banner.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerAdminDTO {
    private Long id;
    private String imageUrl;
    private String link;
    private String alt;
    private Integer displayOrder;
    private boolean isActive;
    private LocalDateTime visibleFrom;
    private LocalDateTime visibleTo;
}