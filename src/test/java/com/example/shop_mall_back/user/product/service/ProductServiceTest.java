package com.example.shop_mall_back.user.product.service;

import com.example.shop_mall_back.admin.product.domain.DeliveryInfo;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.common.domain.Product.SellStatus;
import com.example.shop_mall_back.user.product.domain.Brand;
import com.example.shop_mall_back.user.product.domain.Category;
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
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

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

        Brand brand = Brand.builder()
                .id(1L)
                .name("TestBrand")
                .build();

        Category category = Category.builder()
                .id(1L)
                .name("TestCategory")
                .build();

        DeliveryInfo deliveryInfo = DeliveryInfo.builder()
                .id(1L)
                .delivery_yn("y")
                .deliveryCom(DeliveryInfo.Delivery_com.CJ)
                .deliveryPrice(3000)
                .build();

        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(10000)
                .stock(5)
                .brand(brand)
                .category(category)
                .deliveryInfo(deliveryInfo)
                .sellStatus(SellStatus.판매중)
                .viewCount(0)
                .build();
    }

    @Test
    void getProducts_ShouldReturnPagedProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(testProduct));

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<ProductDto> result = productService.getProducts(pageable);

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Test Product");
    }

    @Test
    void getProductById_ShouldReturnProductAndIncreaseViewCount() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any())).thenReturn(testProduct);

        ProductDto result = productService.getProductById(1L);

        assertThat(result.getName()).isEqualTo("Test Product");
        assertThat(result.getViewCount()).isEqualTo(1);
    }

    @Test
    void searchProducts_ShouldReturnMatchingProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findByNameContaining("Test", pageable)).thenReturn(new PageImpl<>(List.of(testProduct)));

        Page<ProductDto> result = productService.searchProducts("Test", pageable);

        assertThat(result).hasSize(1);
    }

    @Test
    void getProductImages_ShouldReturnImageList() {
        ProductImage image = ProductImage.builder()
                .id(1L)
                .imgName("test.jpg")
                .oriImgName("original.jpg")
                .imgUrl("/images/test.jpg")
                .isRepImg(true)
                .product(testProduct)
                .build();

        when(productImageRepository.findByProductIdOrderByIdAsc(1L)).thenReturn(List.of(image));

        List<ProductImageDto> result = productService.getProductImages(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getImgUrl()).isEqualTo("/images/test.jpg");
    }

    @Test
    void getProductsByCategory_ShouldReturnProductList() {
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findByCategoryId(1L, pageable)).thenReturn(new PageImpl<>(List.of(testProduct)));

        Page<ProductDto> result = productService.getProductsByCategory(1L, pageable);

        assertThat(result).hasSize(1);
    }

    @Test
    void getRecommendedProducts_ShouldReturnSameBrandProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.findByBrandId(1L, pageable)).thenReturn(new PageImpl<>(List.of(testProduct)));

        Page<ProductDto> result = productService.getRecommendedProducts(1L, pageable);

        assertThat(result).hasSize(1);
    }

    @Test
    void getProductsByBrand_ShouldReturnProductList() {
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findByBrandId(1L, pageable)).thenReturn(new PageImpl<>(List.of(testProduct)));

        Page<ProductDto> result = productService.getProductsByBrand(1L, pageable);

        assertThat(result).hasSize(1);
    }

    @Test
    void getProductsByCategoryAndChildren_ShouldReturnProductList() {
        Pageable pageable = PageRequest.of(0, 10);
        when(categoryService.getAllChildCategoryIds(1L)).thenReturn(List.of(1L, 2L));
        when(productRepository.findByCategoryIdIn(List.of(1L, 2L), pageable)).thenReturn(new PageImpl<>(List.of(testProduct)));

        Page<ProductDto> result = productService.getProductsByCategoryAndChildren(1L, pageable);

        assertThat(result).hasSize(1);
    }
}
