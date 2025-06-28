package com.example.shop_mall_back.user.product.dto;

import com.example.shop_mall_back.user.product.domain.WishlistItem;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WishlistItemDto {
    private Long productId;
    private String productName;

    /**
     * WishlistItem 엔티티를 WishlistItemDto로 변환하는 정적 메서드
     * @param item 위시리스트 엔티티 객체
     * @return 변환된 DTO 객체
     */
    public static WishlistItemDto from(WishlistItem item) {
        return WishlistItemDto.builder()
                .productId(item.getProduct().getId())     // 상품 ID 추출
                .productName(item.getProduct().getName()) // 상품 이름 추출
                .build();                                 // DTO 객체 생성
    }
}
