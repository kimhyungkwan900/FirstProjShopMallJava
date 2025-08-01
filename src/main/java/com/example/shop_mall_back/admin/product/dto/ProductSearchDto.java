package com.example.shop_mall_back.admin.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchDto {

    //검색조건 입력: 상품번호 , 상품명, 브랜드명
    //검색조건 선택: 판매상태, 카테고리, 등록일자

    private Long productId;

    private String productName;

    private String brandName;

    private String sellStatus;

    private Long categoryId;

    private String dateType;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
