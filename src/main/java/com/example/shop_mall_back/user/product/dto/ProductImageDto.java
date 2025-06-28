package com.example.shop_mall_back.user.product.dto;

import com.example.shop_mall_back.user.product.domain.ProductImage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductImageDto {
    // 이미지 고유 ID
    private Long id;

    // 서버에 저장된 이미지 파일명 (실제 파일 이름)
    private String imgName;

    // 사용자가 업로드한 원본 이미지 파일명
    private String oriImgName;

    // 이미지가 저장된 경로(URL) - 웹에서 접근 가능한 경로
    private String imgUrl;

    // 대표 이미지 여부 (true: 대표 이미지, false: 서브 이미지)
    private boolean isRepImg;

    public static ProductImageDto from(ProductImage image) {
        return ProductImageDto.builder()
                .id(image.getId())                       // 이미지 ID 복사
                .imgName(image.getImgName())             // 저장된 파일명 복사
                .oriImgName(image.getOriImgName())       // 원본 파일명 복사
                .imgUrl(image.getImgUrl())               // 이미지 URL 복사
                .isRepImg(image.isRepImg())              // 대표 이미지 여부 복사
                .build();                                // DTO 객체 생성 후 반환
    }
}