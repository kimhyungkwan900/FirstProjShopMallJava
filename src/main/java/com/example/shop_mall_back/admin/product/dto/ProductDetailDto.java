package com.example.shop_mall_back.admin.product.dto;

import com.example.shop_mall_back.admin.product.domain.DeliveryInfo;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.product.domain.Brand;
import com.example.shop_mall_back.user.product.domain.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//상품 상세 정보 출력 시 사용하는 DTO
@Getter
public class ProductDetailDto {
    private Long id;

    private String name;

    private String description;

    private int price;

    private int stock = 0;

    private Brand brand;

    private Product.SellStatus sellStatus;

    private DeliveryInfo deliveryInfo;

    private Category category;

    @Setter
    private List<ProductImgDto> productImgDtoList = new ArrayList<>();
}
