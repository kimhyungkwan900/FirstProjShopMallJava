package com.example.shop_mall_back.user.product.controller;

import com.example.shop_mall_back.user.product.dto.BrandDto;
import com.example.shop_mall_back.user.product.service.BrandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(BrandController.class)
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandService brandService;

    @Test
    @DisplayName("GET /api/brands - 전체 브랜드 목록을 반환한다")
    void getAllBrands_ShouldReturnBrandList() throws Exception {
        // Builder 방식으로 BrandDto 객체 생성
        BrandDto brand1 = BrandDto.builder()
                .id(1L)
                .name("아라사카")
                .build();

        BrandDto brand2 = BrandDto.builder()
                .id(2L)
                .name("밀리테크")
                .build();

        List<BrandDto> brandList = List.of(brand1, brand2);

        // Mock 설정
        given(brandService.getAllBrands()).willReturn(brandList);

        // 요청 및 검증
        mockMvc.perform(get("/api/brands")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("아라사카"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("밀리테크"));
    }
}