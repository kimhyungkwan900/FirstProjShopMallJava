package com.example.shop_mall_back.user.product.repository;

import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.product.domain.Brand;
import com.example.shop_mall_back.user.product.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Test
    @DisplayName("상품 이름에 키워드가 포함된 상품을 페이징하여 조회한다")
    void findByNameContaining() {
        // given
        Product p1 = productRepository.save(Product.builder().name("청바지").price(10000).stock(10).build());
        Product p2 = productRepository.save(Product.builder().name("슬림 청바지").price(12000).stock(5).build());
        Product p3 = productRepository.save(Product.builder().name("티셔츠").price(8000).stock(15).build());

        // when
        Page<Product> result = productRepository.findByNameContaining("청바지", PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting("name").contains("청바지", "슬림 청바지");
    }

    @Test
    @DisplayName("특정 브랜드 ID로 상품을 조회한다")
    void findByBrandId() {
        // given
        Brand brand = brandRepository.save(Brand.builder().name("나이키").build());

        Product p1 = productRepository.save(Product.builder().name("나이키 운동화").brand(brand).price(15000).stock(20).build());
        Product p2 = productRepository.save(Product.builder().name("나이키 셔츠").brand(brand).price(18000).stock(10).build());

        // when
        Page<Product> result = productRepository.findByBrandId(brand.getId(), PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).allMatch(p -> p.getBrand().getName().equals("나이키"));
    }

    @Test
    @DisplayName("특정 카테고리 ID로 상품을 조회한다")
    void findByCategoryId() {
        // given
        Category category = categoryRepository.save(Category.builder().name("신발").build());

        Product p1 = productRepository.save(Product.builder().name("운동화").category(category).price(9000).stock(12).build());
        Product p2 = productRepository.save(Product.builder().name("슬리퍼").category(category).price(5000).stock(8).build());

        // when
        Page<Product> result = productRepository.findByCategoryId(category.getId(), PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).allMatch(p -> p.getCategory().getName().equals("신발"));
    }

    @Test
    @DisplayName("여러 카테고리 ID 리스트로 상품을 조회한다")
    void findByCategoryIdIn() {
        // given
        Category c1 = categoryRepository.save(Category.builder().name("상의").build());
        Category c2 = categoryRepository.save(Category.builder().name("하의").build());

        Product p1 = productRepository.save(Product.builder().name("티셔츠").category(c1).price(7000).stock(10).build());
        Product p2 = productRepository.save(Product.builder().name("청바지").category(c2).price(12000).stock(5).build());

        // when
        Page<Product> result = productRepository.findByCategoryIdIn(List.of(c1.getId(), c2.getId()), PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting("category.name").containsExactlyInAnyOrder("상의", "하의");
    }
}
