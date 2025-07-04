package com.example.shop_mall_back.user.product.controller;

import com.example.shop_mall_back.common.config.jwt.TokenProvider;
import com.example.shop_mall_back.user.product.dto.CategoryDto;
import com.example.shop_mall_back.user.product.service.CategoryService;
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
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private CategoryService categoryService;

    @Test
    @DisplayName("GET /api/categories - 전체 카테고리를 리스트로 반환한다")
    void getAllCategories_ShouldReturnFlatList() throws Exception {
        // given
        List<CategoryDto> categories = List.of(
                CategoryDto.builder()
                        .id(1L)
                        .name("패션")
                        .children(List.of())
                        .build(),
                CategoryDto.builder()
                        .id(2L)
                        .name("전자기기")
                        .children(List.of())
                        .build()
        );

        given(categoryService.getAllCategories()).willReturn(categories);

        // when & then
        mockMvc.perform(get("/api/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("패션"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("전자기기"));
    }

    @Test
    @DisplayName("GET /api/categories/tree - 카테고리를 트리 구조로 반환한다")
    void getCategoryTree_ShouldReturnNestedTree() throws Exception {
        // given: "패션" 카테고리 아래에 "상의"가 포함된 트리 구조
        List<CategoryDto> tree = List.of(
                CategoryDto.builder()
                        .id(1L)
                        .name("패션")
                        .children(List.of(
                                CategoryDto.builder()
                                        .id(2L)
                                        .name("상의")
                                        .children(List.of())
                                        .build()
                        ))
                        .build()
        );

        given(categoryService.getCategoryTree()).willReturn(tree);

        // when & then
        mockMvc.perform(get("/api/categories/tree")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("패션"))
                .andExpect(jsonPath("$[0].children[0].id").value(2L))
                .andExpect(jsonPath("$[0].children[0].name").value("상의"));
    }
}