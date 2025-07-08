package com.example.shop_mall_back.admin.product.servicetest;

import com.example.shop_mall_back.admin.product.dto.ProductFormDto;
import com.example.shop_mall_back.admin.product.repository.AdminProductImgRepository;
import com.example.shop_mall_back.admin.product.repository.AdminProductRepository;
import com.example.shop_mall_back.admin.product.repository.DeliveryInfoRepository;
import com.example.shop_mall_back.admin.product.service.ProductImgService;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.product.repository.BrandRepository;
import com.example.shop_mall_back.user.product.repository.CategoryRepository;
import com.example.shop_mall_back.user.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdminProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AdminProductRepository adminProductRepository;

    @Mock
    private AdminProductImgRepository adminProductImgRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private DeliveryInfoRepository deliveryInfoRepository;

    @Mock
    private ProductImgService productImgService;

    private ModelMapper modelMapper;

    public AdminProductServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveProductTest() {
        //given
        MockMultipartFile file1 = new MockMultipartFile(
                "img1", "img1.png", "images/png", "img1Content".getBytes()
        );
        MockMultipartFile file2 = new MockMultipartFile(
                "img2", "img2.png", "images/png", "img2Content".getBytes()
        );

        List<MultipartFile> files = List.of(file1, file2);

        ProductFormDto productFormDto = new ProductFormDto().builder()
                .name("테스트1")
                .price(20000)
                .description("테스트1 상세설명입니다.")
                .stock(500)
                .categoryId(1L)
                .brandId(1L)
                .deliveryInfoId(1L)
                .sellStatus(Product.SellStatus.판매중)
                .productImgIds(new ArrayList<Long>())
                .build();


    }

    @Test
    void getAdminProductPageTest() {
    }

    @Test
    void getProductDetailTest() {
    }

    @Test
    void updateProductTest() {
    }

    @Test
    void deleteProductTest() {
    }
}