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

    public List<BrandDto> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(BrandDto::from)
                .collect(Collectors.toList());
    }
}