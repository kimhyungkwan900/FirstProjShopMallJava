package com.example.shop_mall_back.user.product.service;

import com.example.shop_mall_back.user.product.domain.Brand;
import com.example.shop_mall_back.user.product.domain.Category;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.product.domain.ProductImage;
import com.example.shop_mall_back.user.product.dto.ProductDto;
import com.example.shop_mall_back.user.product.dto.ProductImageDto;
import com.example.shop_mall_back.user.product.repository.ProductImageRepository;
import com.example.shop_mall_back.user.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AdminProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @Mock
    private CategoryService categoryService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder()
                .id(1L)
                .name("테스트 상품")
                .description("상세 설명")
                .price(10000)
                .stock(20)
                .viewCount(5)
                .brand(Brand.builder().id(1L).name("아라사카").build())
                .category(Category.builder().id(1L).name("상의").build())
                .build();
    }

    @Test
    @DisplayName("전체 상품을 페이징 조회한다")
    void getProducts_shouldReturnPagedProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(sampleProduct));
        given(productRepository.findAll(pageable)).willReturn(page);

        Page<ProductDto> result = productService.getProducts(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("테스트 상품");
    }

    @Test
    @DisplayName("상품 ID로 상세 정보를 조회하면 조회수가 증가한다")
    void getProductById_shouldIncreaseViewCountAndReturnProduct() {
        given(productRepository.findById(1L)).willReturn(Optional.of(sampleProduct));
        given(productRepository.save(any())).willReturn(sampleProduct);

        ProductDto result = productService.getProductById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(sampleProduct.getViewCount()).isEqualTo(6);
    }

    @Test
    @DisplayName("키워드로 상품을 검색한다")
    void searchProducts_shouldReturnMatchingProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(sampleProduct));
        given(productRepository.findByNameContaining("테스트", pageable)).willReturn(page);

        Page<ProductDto> result = productService.searchProducts("테스트", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("브랜드 ID로 추천 상품을 조회한다")
    void getRecommendedProducts_shouldReturnSameBrandProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        given(productRepository.findById(1L)).willReturn(Optional.of(sampleProduct));
        given(productRepository.findByBrandId(1L, pageable))
                .willReturn(new PageImpl<>(List.of(sampleProduct)));

        Page<ProductDto> result = productService.getRecommendedProducts(1L, pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("카테고리 ID로 상품을 조회한다")
    void getProductsByCategory_shouldReturnFilteredByCategory() {
        Pageable pageable = PageRequest.of(0, 10);
        given(productRepository.findByCategoryId(1L, pageable))
                .willReturn(new PageImpl<>(List.of(sampleProduct)));

        Page<ProductDto> result = productService.getProductsByCategory(1L, pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("브랜드 ID로 상품을 조회한다")
    void getProductsByBrand_shouldReturnFilteredByBrand() {
        Pageable pageable = PageRequest.of(0, 10);
        given(productRepository.findByBrandId(1L, pageable))
                .willReturn(new PageImpl<>(List.of(sampleProduct)));

        Page<ProductDto> result = productService.getProductsByBrand(1L, pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("상품 이미지 목록을 조회한다")
    void getProductImages_shouldReturnImageList() {
        ProductImage image = ProductImage.builder()
                .id(1L)
                .imgUrl("image.jpg")
                .isRepImg(true)
                .product(sampleProduct)
                .build();

        given(productImageRepository.findByProductIdOrderByIdAsc(1L))
                .willReturn(List.of(image));

        List<ProductImageDto> result = productService.getProductImages(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getImgUrl()).isEqualTo("image.jpg");
    }

    @Test
    @DisplayName("하위 카테고리를 포함한 상품을 조회한다")
    void getProductsByCategoryAndChildren_shouldReturnByAllSubCategories() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> ids = List.of(1L, 2L, 3L);
        given(categoryService.getAllChildCategoryIds(1L)).willReturn(ids);
        given(productRepository.findByCategoryIdIn(ids, pageable))
                .willReturn(new PageImpl<>(List.of(sampleProduct)));

        Page<ProductDto> result = productService.getProductsByCategoryAndChildren(1L, pageable);

        assertThat(result.getContent()).hasSize(1);
    }
}