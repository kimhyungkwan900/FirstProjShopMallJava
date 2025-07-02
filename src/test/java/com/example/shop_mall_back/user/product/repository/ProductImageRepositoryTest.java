package com.example.shop_mall_back.user.product.repository;

import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.product.domain.ProductImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductImageRepositoryTest {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 ID로 모든 이미지를 ID 오름차순으로 조회")
    void findAllImagesByProductIdAsc() {
        // given
        Product product = productRepository.save(Product.builder()
                .name("샘플 상품")
                .description("테스트 설명")
                .price(10000)
                .stock(5)
                .build());

        ProductImage img1 = ProductImage.builder()
                .product(product)
                .imgName("img1.jpg")
                .oriImgName("original1.jpg")
                .imgUrl("http://test.com/img1.jpg")
                .isRepImg(false)
                .build();

        ProductImage img2 = ProductImage.builder()
                .product(product)
                .imgName("img2.jpg")
                .oriImgName("original2.jpg")
                .imgUrl("http://test.com/img2.jpg")
                .isRepImg(false)
                .build();

        productImageRepository.saveAll(List.of(img1, img2));

        // when
        List<ProductImage> result = productImageRepository.findByProductIdOrderByIdAsc(product.getId());

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getImgName()).isEqualTo("img1.jpg");
        assertThat(result.get(1).getImgName()).isEqualTo("img2.jpg");
    }

    @Test
    @DisplayName("대표 이미지를 조회")
    void findRepresentativeImage() {
        // given
        Product product = productRepository.save(Product.builder()
                .name("대표 이미지 상품")
                .description("대표 이미지 테스트")
                .price(20000)
                .stock(3)
                .build());

        ProductImage repImg = ProductImage.builder()
                .product(product)
                .imgName("rep.jpg")
                .oriImgName("original_rep.jpg")
                .imgUrl("http://test.com/rep.jpg")
                .isRepImg(true)
                .build();

        ProductImage otherImg = ProductImage.builder()
                .product(product)
                .imgName("other.jpg")
                .oriImgName("original_other.jpg")
                .imgUrl("http://test.com/other.jpg")
                .isRepImg(false)
                .build();

        productImageRepository.saveAll(List.of(repImg, otherImg));

        // when
        ProductImage result = productImageRepository.findByProductIdAndIsRepImgTrue(product.getId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getImgName()).isEqualTo("rep.jpg");
        assertThat(result.isRepImg()).isTrue();
    }
}
