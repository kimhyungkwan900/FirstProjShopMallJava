package com.example.shop_mall_back.user.product.domain;

import com.example.shop_mall_back.common.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "products_img")
@Builder
@AllArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName;     // 서버 저장용 파일명
    private String oriImgName;  // 업로드된 원본 파일명
    private String imgUrl;      // 이미지 조회 경로
    private boolean isRepImg;   // 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id") // FK
    private Product product;

    public ProductImage(Product product, Boolean isRepImg) {
        this.product = product;
        this.isRepImg = isRepImg;
    }

    // 원본이미지 파일명, 업데이트할 이미지 파일명, 이미지 경로
    public void updateProductImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
