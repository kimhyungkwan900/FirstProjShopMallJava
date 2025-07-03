package com.example.shop_mall_back.user.product.service;

import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.common.domain.Product.SellStatus;
import com.example.shop_mall_back.user.product.domain.Brand;
import com.example.shop_mall_back.user.product.domain.ProductImage;
import com.example.shop_mall_back.user.product.dto.ProductDto;
import com.example.shop_mall_back.user.product.dto.ProductImageDto;
import com.example.shop_mall_back.user.product.repository.ProductImageRepository;
import com.example.shop_mall_back.user.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AdminProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // ✅ 브랜드 객체 추가
        Brand testBrand = Brand.builder()
                .id(1L)
                .name("Test Brand")
                .build();

        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(10000)
                .stock(10)
                .viewCount(0)
                .sellStatus(SellStatus.판매중)
                .brand(testBrand) // ✅ 브랜드 설정
                .build();
    }

    @Test
    void getProducts_ShouldReturnPagedProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(testProduct));

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<ProductDto> result = productService.getProducts(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Test Product");
    }

    @Test
    void getProductById_ShouldReturnProductWithIncreasedViewCount() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any())).thenReturn(testProduct);

        ProductDto result = productService.getProductById(1L);

        assertThat(result.getName()).isEqualTo("Test Product");
        assertThat(result.getViewCount()).isEqualTo(1); // viewCount 증가 확인
    }

    @Test
    void searchProducts_ShouldReturnMatchingProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findByNameContaining("Test", pageable))
                .thenReturn(new PageImpl<>(List.of(testProduct)));

        Page<ProductDto> result = productService.searchProducts("Test", pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).contains("Test");
    }

    @Test
    void getProductImages_ShouldReturnImageList() {
        ProductImage image = ProductImage.builder()
                .id(1L)
                .imgName("img1.jpg")
                .oriImgName("original.jpg")
                .imgUrl("/images/img1.jpg")
                .isRepImg(true)
                .product(testProduct)
                .build();

        when(productImageRepository.findByProductIdOrderByIdAsc(1L)).thenReturn(List.of(image));

        List<ProductImageDto> result = productService.getProductImages(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getImgUrl()).isEqualTo("/images/img1.jpg");
    }
}
