package com.example.shop_mall_back.user.product.service;

import com.example.shop_mall_back.user.product.domain.Category;
import com.example.shop_mall_back.user.product.dto.CategoryDto;
import com.example.shop_mall_back.user.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 전체 카테고리를 단순한 리스트 형태로 조회 (계층 구조 아님)
     * → 주로 관리자용 리스트나 단순 출력용
     */
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 상위(최상위) 카테고리를 기준으로 트리 구조의 카테고리 계층 반환
     * → 주로 사용자 UI에서 드롭다운, 메뉴, 필터 구조 등에 사용
     */
    public List<CategoryDto> getCategoryTree() {
        List<Category> topCategories = categoryRepository.findByParentIsNull();
        return topCategories.stream()
                .map(CategoryDto::from)
                .collect(Collectors.toList());
    }

    /*
      특정 카테고리 ID를 기준으로 본인 + 하위 카테고리의 ID 목록을 모두 반환
      → 필터 조건에서 '여성의류 전체'를 포함하려 할 때 유용
     */
    public List<Long> getAllChildCategoryIds(Long parentId) {
        List<Long> ids = new ArrayList<>();
        collectChildCategoryIds(parentId, ids);
        return ids;
    }

    /*
      재귀적으로 자식 카테고리의 ID를 수집하는 헬퍼 메서드
      - 본인 ID 포함
      - 하위 자식이 있을 경우 깊이 탐색
     */
    private void collectChildCategoryIds(Long parentId, List<Long> result) {
        result.add(parentId); // 본인 카테고리 ID 추가
        List<Category> children = categoryRepository.findByParentId(parentId);// 직계 자식 카테고리 조회
        for (Category child : children) {
            collectChildCategoryIds(child.getId(), result); // 자식의 자식도 재귀적으로 수집
        }
    }
}