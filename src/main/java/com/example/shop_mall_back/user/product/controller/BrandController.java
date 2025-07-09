package com.example.shop_mall_back.user.product.controller;

import com.example.shop_mall_back.user.product.dto.BrandDto;
import com.example.shop_mall_back.user.product.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    /*
      전체 브랜드 목록을 조회합니다.
      GET /api/brands
      @return List<BrandDto> - 모든 브랜드 정보를 담은 DTO 리스트
     */
    @GetMapping
    public List<BrandDto> getAllBrands() {
        return brandService.getAllBrands();
    }

}