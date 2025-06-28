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
                .children(category.getChildren() != null ? // 자식 카테고리가 존재하면 재귀적으로 from() 호출하여 트리 구조 구성
                        category.getChildren().stream() // 자식 카테고리를 재귀적으로 DTO로 변환
                                .map(CategoryDto::from) // 리스트로 수집
                                .collect(Collectors.toList())
                        : List.of()) // 자식이 없으면 빈 리스트
                .build();
    }
}