package com.example.shop_mall_back.user.product.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.shop_mall_back.user.product.dto.ProductDto;
import com.example.shop_mall_back.user.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private final ProductDto sampleProduct = ProductDto.builder()
            .id(1L)
            .name("샘플 상품")
            .description("샘플 설명")
            .price(15000)
            .stock(20)
            .brandName("아라사카")
            .viewCount(100)
            .sellStatus("판매중")
            .deliveryInfo("기본 배송")
            .categoryName("상의")
            .build();

    @Test
    @DisplayName("GET /api/products - 전체 상품 목록을 페이징 처리하여 반환한다")
    void listProducts_ShouldReturnProductList() throws Exception {
        Page<ProductDto> productPage = new PageImpl<>(List.of(sampleProduct));
        given(productService.getProducts(PageRequest.of(0, 10))).willReturn(productPage);

        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("샘플 상품"));
    }

    @Test
    @DisplayName("GET /api/products/{id} - 특정 상품 상세 정보를 반환한다")
    void getProductById_ShouldReturnProduct() throws Exception {
        given(productService.getProductById(1L)).willReturn(sampleProduct);
        given(productService.getProductImages(1L)).willReturn(List.of());

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("샘플 상품"));
    }

    @Test
    @DisplayName("GET /api/products/search?keyword=샘플 - 키워드 검색 결과를 반환한다")
    void searchProducts_ShouldReturnMatchingProducts() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDto> productPage = new PageImpl<>(List.of(sampleProduct));
        given(productService.searchProducts("샘플", pageable)).willReturn(productPage);

        mockMvc.perform(get("/api/products/search")
                        .param("keyword", "샘플")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("샘플 상품"));
    }

    @Test
    @DisplayName("GET /api/products/filter?categoryId=1 - 필터 조건으로 상품을 검색한다")
    void filterProducts_ShouldReturnFilteredProducts() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<ProductDto> productPage = new PageImpl<>(List.of(sampleProduct));
        given(productService.filterProducts(
                Optional.of(1L), Optional.empty(), Optional.empty(), Optional.empty(), pageable))
                .willReturn(productPage);

        mockMvc.perform(get("/api/products/filter")
                        .param("categoryId", "1")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].categoryName").value("상의"));
    }
}