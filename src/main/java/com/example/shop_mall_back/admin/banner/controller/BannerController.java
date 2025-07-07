package com.example.shop_mall_back.admin.banner.controller;

import com.example.shop_mall_back.admin.banner.dto.BannerDTO;
import com.example.shop_mall_back.admin.banner.service.serviceInterface.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping("/main")
    public ResponseEntity<List<BannerDTO>> getMainBanners() {
        List<BannerDTO> banners = bannerService.getVisibleBanners();
        return ResponseEntity.ok(banners);
    }
}
