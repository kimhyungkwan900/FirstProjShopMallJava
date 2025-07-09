package com.example.shop_mall_back.user.product.repository;

import com.example.shop_mall_back.user.product.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    /*
      특정 상품 ID에 해당하는 모든 이미지 목록을 ID 오름차순으로 조회
      - 상품 상세 페이지에서 이미지들을 순서대로 보여줄 때 사용
      - 예: SELECT * FROM product_image WHERE product_id = ? ORDER BY id ASC
     */
    List<ProductImage> findByProductIdOrderByIdAsc(Long productId);

    /*
      특정 상품의 대표 이미지를 조회
      - 상품 목록이나 썸네일에서 대표 이미지 1장을 보여줄 때 사용
      - 예: SELECT * FROM product_image WHERE product_id = ? AND is_rep_img = true
     */
    ProductImage findByProductIdAndIsRepImgTrue(Long productId);
}