package com.example.shop_mall_back.user.product.dto;

import com.example.shop_mall_back.user.product.domain.Category;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CategoryDto {
    private Long id;
    private String name;
    private List<CategoryDto> children;

    public static CategoryDto from(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .children(category.getChildren() != null ?
                        category.getChildren().stream()
                                .map(CategoryDto::from)
                                .collect(Collectors.toList()) : List.of())
                .build();
    }
}