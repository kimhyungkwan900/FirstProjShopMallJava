package com.example.shop_mall_back.user.product.service;

import com.example.shop_mall_back.user.product.domain.Brand;
import com.example.shop_mall_back.user.product.dto.BrandDto;
import com.example.shop_mall_back.user.product.repository.BrandRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;

    @Test
    @DisplayName("getAllBrands - 전체 브랜드를 DTO 형태로 반환한다")
    void getAllBrands_ShouldReturnBrandDtoList() {
        // given
        Brand brand1 = Brand.builder().id(1L).name("아라사카").build();
        Brand brand2 = Brand.builder().id(2L).name("밀리테크").build();

        when(brandRepository.findAll()).thenReturn(List.of(brand1, brand2));

        // when
        List<BrandDto> result = brandService.getAllBrands();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("아라사카");
        assertThat(result.get(1).getName()).isEqualTo("밀리테크");
    }
}
