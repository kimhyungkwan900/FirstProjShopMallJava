package com.example.shop_mall_back.user.product.service;

import com.example.shop_mall_back.user.product.domain.Category;
import com.example.shop_mall_back.user.product.dto.CategoryDto;
import com.example.shop_mall_back.user.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryDto::from)
                .collect(Collectors.toList());
    }

    public List<CategoryDto> getCategoryTree() {
        List<Category> topCategories = categoryRepository.findByParentIsNull();
        return topCategories.stream()
                .map(CategoryDto::from)
                .collect(Collectors.toList());
    }
}