package com.example.shop_mall_back.user.product.dto;

import com.example.shop_mall_back.common.domain.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data // Lombok 어노테이션: getter, setter, equals, hashCode, toString 자동 생성
@Builder // Lombok 어노테이션: 빌더 패턴으로 객체 생성 가능
public class ProductDto {

    private Long id; // 상품 고유 ID
    private String name; // 상품명
    private String description; // 상품 설명
    private int price; // 상품 가격
    private int stock; // 상품 재고
    private String brandName; // 브랜드 이름 (Brand 객체 대신 이름만 노출)
    private int viewCount; // 상품 조회수
    private String sellStatus; // 판매 상태 (예: "판매중", "품절")
    private String deliveryInfo; // 배송사 정보 (예: "CJ", "LOGEN")
    private String categoryName; // 카테고리 이름 (단일 카테고리 기준)
    private List<ProductImageDto> images; // 상품 이미지 리스트 (ProductImageDto 형태로 변환됨)

    // Entity(Product)를 기반으로 ProductDto로 변환하는 정적 메서드
    public static ProductDto from(Product product) {
        return ProductDto.builder()
                .id(product.getId()) // 상품 ID
                .name(product.getName()) // 상품명
                .description(product.getDescription()) // 상품 설명
                .price(product.getPrice()) // 가격
                .stock(product.getStock()) // 재고 수량
                .brandName(product.getBrand().getName()) // 브랜드 이름 추출
                .viewCount(product.getViewCount()) // 조회수
                .sellStatus(product.getSellStatus().name()) // 판매 상태 (Enum → String)
                .deliveryInfo(product.getDeliveryInfo().getDeliveryCom().name()) // 배송사 이름 (Enum → String)
                .categoryName(product.getCategory().getName()) // 카테고리 이름
                .images( // 이미지 리스트를 ProductImageDto 리스트로 변환
                        product.getImages().stream()
                                .map(ProductImageDto::from) // 각 ProductImage → ProductImageDto로 매핑
                                .collect(Collectors.toList()) // 리스트로 수집
                )
                .build(); // 빌더로 ProductDto 객체 생성
    }
}


