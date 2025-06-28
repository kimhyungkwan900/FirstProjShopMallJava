package com.example.shop_mall_back.user.product.service;

import com.example.shop_mall_back.user.product.dto.BrandDto;
import com.example.shop_mall_back.user.product.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    /**
     * 전체 브랜드 목록을 조회하여 DTO 형태로 반환
     * - 주로 사용자 페이지에서 브랜드 필터 드롭다운 구성 등에 사용
     * - Entity → DTO 변환을 통해 필요한 데이터만 전달
     */
    public List<BrandDto> getAllBrands() {
        return brandRepository.findAll().stream()         // 브랜드 전체 조회
                .map(BrandDto::from)                      // 각 엔티티를 DTO로 변환
                .collect(Collectors.toList());            // 리스트로 수집 후 반환
    }
}