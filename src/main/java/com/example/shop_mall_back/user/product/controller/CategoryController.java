package com.example.shop_mall_back.user.product.controller;

import com.example.shop_mall_back.user.product.dto.CategoryDto;
import com.example.shop_mall_back.user.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 모든 카테고리 데이터를 단순 리스트 형태로 반환하는 GET API
    // 요청 예: GET /api/categories
    @GetMapping
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();// CategoryService에서 모든 카테고리를 불러와 응답
    }

    // 트리 구조(계층형)로 카테고리 데이터를 반환하는 GET API
    // 요청 예: GET /api/categories/tree
    @GetMapping("/tree")
    public List<CategoryDto> getCategoryTree() {
        return categoryService.getCategoryTree();// CategoryService에서 계층형 구조로 가공된 카테고리 데이터를 불러와 응답
    }
}