package com.example.shop_mall_back.user.product.repository;

import com.example.shop_mall_back.user.product.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("findByParentIsNull(): 최상위 카테고리를 조회한다")
    void findTopLevelCategories() {
        // given
        Category fashion = Category.builder().name("패션").build();
        Category electronics = Category.builder().name("전자기기").build();
        categoryRepository.saveAll(List.of(fashion, electronics));

        // when
        List<Category> topCategories = categoryRepository.findByParentIsNull();

        // then
        assertThat(topCategories).hasSize(2);
        assertThat(topCategories).extracting(Category::getName)
                .containsExactlyInAnyOrder("패션", "전자기기");
    }

    @Test
    @DisplayName("findByParentId(): 특정 상위 카테고리의 하위 카테고리를 조회한다")
    void findSubcategoriesByParentId() {
        // given
        Category parent = categoryRepository.save(Category.builder().name("패션").build());

        Category top = Category.builder().name("상의").parent(parent).build();
        Category bottom = Category.builder().name("하의").parent(parent).build();
        categoryRepository.saveAll(List.of(top, bottom));

        // when
        List<Category> subcategories = categoryRepository.findByParentId(parent.getId());

        // then
        assertThat(subcategories).hasSize(2);
        assertThat(subcategories).extracting(Category::getName)
                .containsExactlyInAnyOrder("상의", "하의");
    }
}
