package com.example.shop_mall_back.admin.product.servicetest;

import com.example.shop_mall_back.admin.product.dto.ProductFormDto;
import com.example.shop_mall_back.admin.product.repository.AdminProductImgRepository;
import com.example.shop_mall_back.admin.product.service.AdminProductService;
import com.example.shop_mall_back.admin.product.service.ProductImgService;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.product.domain.ProductImage;
import com.example.shop_mall_back.user.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminProductServiceTest {

    @Autowired
    private AdminProductService adminProductService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AdminProductImgRepository adminProductImgRepository;

    @Mock
    private ProductImgService productImgService;

    private ModelMapper modelMapper;

//    public AdminProductServiceTest() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    void saveProductTest() throws Exception{
        //given
        MockMultipartFile file1 = new MockMultipartFile(
                "img1", "img1.png", "images/png", "img1Content".getBytes()
        );
        MockMultipartFile file2 = new MockMultipartFile(
                "img2", "img2.png", "images/png", "img2Content".getBytes()
        );

        List<MultipartFile> productImgFileList = List.of(file1, file2);

        ProductFormDto productFormDto = new ProductFormDto(
                1L,
                "테스트1",
                20000,
                "테스트1 상세설명입니다.",
                500,
                1L,
                1L,
                1L,
                Product.SellStatus.판매중,
                new ArrayList<Long>()
        );

        System.out.println(productFormDto);

        Long productId = adminProductService.saveProduct(productFormDto, productImgFileList);
        List<ProductImage> productImgList = adminProductImgRepository.findByProductIdOrderByIdAsc(productId);

        System.out.println("저장된 상품ID: " + productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);

        assertThat(productFormDto.getId()).isEqualTo(product.getId());
        assertThat(productFormDto.getName()).isEqualTo(product.getName());
        assertThat(productFormDto.getPrice()).isEqualTo(product.getPrice());
        assertThat(productFormDto.getStock()).isEqualTo(product.getStock());
        assertThat(productFormDto.getBrandId()).isEqualTo(product.getBrand().getId());
        assertThat(productFormDto.getCategoryId()).isEqualTo(product.getCategory().getId());
        assertThat(productFormDto.getDeliveryInfoId()).isEqualTo(product.getDeliveryInfo().getId());
        assertThat(productFormDto.getSellStatus()).isEqualTo(product.getSellStatus());
        assertThat(productFormDto.getDescription()).isEqualTo(product.getDescription());
        assertThat(productImgFileList.get(0).getOriginalFilename()).isEqualTo(productImgList.get(0).getOriImgName());
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