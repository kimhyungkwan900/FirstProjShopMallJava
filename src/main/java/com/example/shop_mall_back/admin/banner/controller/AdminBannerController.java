package com.example.shop_mall_back.admin.banner.controller;

import com.example.shop_mall_back.admin.banner.dto.BannerRequestDTO;
import com.example.shop_mall_back.admin.banner.service.serviceInterface.BannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/banners")
@RequiredArgsConstructor
public class AdminBannerController {
    private final BannerService bannerService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid BannerRequestDTO dto) {
        Long id = bannerService.createBanner(dto);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid BannerRequestDTO dto) {
        bannerService.updateBanner(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BannerRequestDTO> getBanner(@PathVariable Long id) {
        BannerRequestDTO dto = bannerService.getBannerById(id);
        return ResponseEntity.ok(dto);
    }
}
