package com.example.shop_mall_back.user.product.service;

import com.example.shop_mall_back.user.product.domain.Category;
import com.example.shop_mall_back.user.product.dto.CategoryDto;
import com.example.shop_mall_back.user.product.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category topCategory;
    private Category subCategory1;
    private Category subCategory2;

    @BeforeEach
    void setUp() {
        topCategory = Category.builder().id(1L).name("여성의류").build();
        subCategory1 = Category.builder().id(2L).name("블라우스").parent(topCategory).build();
        subCategory2 = Category.builder().id(3L).name("치마").parent(topCategory).build();
    }

    @Test
    void getAllCategories_ShouldReturnAllCategoryDtos() {
        when(categoryRepository.findAll()).thenReturn(List.of(topCategory, subCategory1, subCategory2));

        List<CategoryDto> result = categoryService.getAllCategories();

        assertThat(result).hasSize(3);
        assertThat(result).extracting("name").contains("여성의류", "블라우스", "치마");
    }

    @Test
    void getCategoryTree_ShouldReturnOnlyTopLevelCategories() {
        when(categoryRepository.findByParentIsNull()).thenReturn(List.of(topCategory));

        List<CategoryDto> result = categoryService.getCategoryTree();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("여성의류");
    }

    @Test
    void getAllChildCategoryIds_ShouldReturnRecursiveIds() {
        when(categoryRepository.findByParentId(1L)).thenReturn(List.of(subCategory1, subCategory2));
        when(categoryRepository.findByParentId(2L)).thenReturn(Collections.emptyList());
        when(categoryRepository.findByParentId(3L)).thenReturn(Collections.emptyList());

        List<Long> result = categoryService.getAllChildCategoryIds(1L);

        assertThat(result).containsExactlyInAnyOrder(1L, 2L, 3L);
    }
}
