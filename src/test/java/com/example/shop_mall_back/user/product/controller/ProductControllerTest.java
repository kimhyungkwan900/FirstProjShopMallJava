package com.example.shop_mall_back.user.product.controller;

import com.example.shop_mall_back.user.product.dto.ProductDto;
import com.example.shop_mall_back.user.product.dto.ProductImageDto;
import com.example.shop_mall_back.user.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private final ProductDto sampleProduct = ProductDto.builder()
            .id(1L)
            .name("샘플 상품")
            .description("샘플 설명")
            .price(10000)
            .stock(10)
            .brandName("브랜드")
            .viewCount(5)
            .sellStatus("판매중")
            .deliveryInfo("기본 배송")
            .categoryName("카테고리")
            .build();

    @Test
    @DisplayName("GET /api/products - 전체 상품 목록 조회")
    void listProducts() throws Exception {
        Page<ProductDto> page = new PageImpl<>(List.of(sampleProduct));
        given(productService.getProducts(any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("샘플 상품"));
    }

    @Test
    @DisplayName("GET /api/products/{id} - 상품 상세 조회")
    void getProduct() throws Exception {
        given(productService.getProductById(1L)).willReturn(sampleProduct);
        given(productService.getProductImages(1L)).willReturn(List.of(
                ProductImageDto.builder().id(101L).imgUrl("http://test.com/1.jpg").isRepImg(true).build()
        ));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("샘플 상품"))
                .andExpect(jsonPath("$.images[0].imgUrl").value("http://test.com/1.jpg"));
    }

    @Test
    @DisplayName("GET /api/products/search?keyword=샘플 - 상품 검색")
    void searchProducts() throws Exception {
        Page<ProductDto> page = new PageImpl<>(List.of(sampleProduct));
        given(productService.searchProducts(eq("샘플"), any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/api/products/search")
                        .param("keyword", "샘플")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("샘플 상품"));
    }

    @Test
    @DisplayName("GET /api/products/filter - 필터 검색")
    void filterProducts() throws Exception {
        Page<ProductDto> productPage = new PageImpl<>(List.of(sampleProduct)); // 이름을 productPage로 맞춰줌

        given(productService.filterProducts(
                eq(Optional.of(1L)),
                eq(Optional.empty()),
                eq(Optional.empty()),
                eq(Optional.empty()),
                any(Pageable.class)
        )).willReturn(productPage);

        mockMvc.perform(get("/api/products/filter")
                        .param("categoryId", "1")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }


    @Test
    @DisplayName("GET /api/products/popular - 인기 상품")
    void getPopularProducts() throws Exception {
        Page<ProductDto> page = new PageImpl<>(List.of(sampleProduct));
        given(productService.getProducts(any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/api/products/popular")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].viewCount").value(5));
    }

    @Test
    @DisplayName("GET /api/products/recent - 최신 상품")
    void getRecentProducts() throws Exception {
        Page<ProductDto> page = new PageImpl<>(List.of(sampleProduct));
        given(productService.getProducts(any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/api/products/recent")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }

    @Test
    @DisplayName("GET /api/products/recommend?productId=1 - 추천 상품")
    void getRecommendedProducts() throws Exception {
        Page<ProductDto> page = new PageImpl<>(List.of(sampleProduct));
        given(productService.getRecommendedProducts(eq(1L), any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/api/products/recommend")
                        .param("productId", "1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].brandName").value("브랜드"));
    }

    @Test
    @DisplayName("GET /api/products/category/1 - 카테고리별 상품")
    void getProductsByCategory() throws Exception {
        Page<ProductDto> page = new PageImpl<>(List.of(sampleProduct));
        given(productService.getProductsByCategory(eq(1L), any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/api/products/category/1")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].categoryName").value("카테고리"));
    }

    @Test
    @DisplayName("GET /api/products/brand/1 - 브랜드별 상품")
    void getProductsByBrand() throws Exception {
        Page<ProductDto> page = new PageImpl<>(List.of(sampleProduct));
        given(productService.getProductsByBrand(eq(1L), any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/api/products/brand/1")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].brandName").value("브랜드"));
    }

    @Test
    @DisplayName("GET /api/products/category/1/all - 하위 포함 전체 카테고리 상품")
    void getProductsByCategoryAndChildren() throws Exception {
        Page<ProductDto> page = new PageImpl<>(List.of(sampleProduct));
        given(productService.getProductsByCategoryAndChildren(eq(1L), any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/api/products/category/1/all")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("샘플 상품"));
    }
}