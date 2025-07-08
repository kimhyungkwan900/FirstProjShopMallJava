package com.example.shop_mall_back.user.myOrder.dto;

import com.example.shop_mall_back.user.product.dto.ProductImageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductDTO {

    private Long id;
    private String name;
    private ProductImageDto image;
    private Integer price;
}
