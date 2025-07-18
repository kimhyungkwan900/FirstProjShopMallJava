package com.example.shop_mall_back.admin.product.dto;

import com.example.shop_mall_back.common.domain.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFormDto {
    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String name;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
    private String description;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stock;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Long categoryId;

    private Long brandId;

    //배송정보
    private Long deliveryInfoId;

    private Product.SellStatus sellStatus;

    private List<Long> productImgIds = new ArrayList<>();

    public Product createProduct(){

        return Product.builder()
                .name(name)
                .price(price)
                .description(description)
                .stock(stock)
                .sellStatus(sellStatus)
                .build();
    }
}
