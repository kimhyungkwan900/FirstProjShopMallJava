package com.example.shop_mall_back.admin.banner.service.serviceInterface;

import com.example.shop_mall_back.admin.banner.domain.Banner;
import com.example.shop_mall_back.admin.banner.dto.BannerDTO;
import com.example.shop_mall_back.admin.banner.dto.BannerRequestDTO;

import java.util.List;

public interface BannerService {
    List<BannerDTO> getVisibleBanners();

    Long createBanner(BannerRequestDTO dto);

    void updateBanner(Long id, BannerRequestDTO dto);

    void deleteBanner(Long id);

    BannerRequestDTO getBannerById(Long id);

    default BannerRequestDTO entityToRequestDTO(Banner banner){
        return  BannerRequestDTO.builder()
                .imageUrl(banner.getImageUrl())
                .link(banner.getLink())
                .alt(banner.getAlt())
                .displayOrder(banner.getDisplayOrder())
                .isActive(banner.isActive())
                .visibleFrom(banner.getVisibleFrom())
                .visibleTo(banner.getVisibleTo())
                .build();
    }

    default Banner dtoToBanner(BannerRequestDTO dto){
        return Banner.builder()
                .imageUrl(dto.getImageUrl())
                .link(dto.getLink())
                .alt(dto.getAlt())
                .displayOrder(dto.getDisplayOrder())
                .isActive(dto.isActive())
                .visibleFrom(dto.getVisibleFrom())
                .visibleTo(dto.getVisibleTo())
                .build();
    }
}
