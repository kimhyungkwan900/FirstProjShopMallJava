package com.example.shop_mall_back.user.Cart.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CartItemDto {
    private Long id;    //장바구니 항목 id
    private Long cart_id;   //장바구니 id
    private Long product_id;    //상품 id
    private int quantity;   //상품 수량
    private String selected_option; //선택한 옵션
    private boolean is_selected;    //선택 여부
    private boolean is_sold_out;    //품절 여부

    private String brandName;   //브랜드 이름
    private String imageUrl;    //상품 이미지
    private String productTitle;    //상품 이름
    private String productPrice;    //상품 가격

    public void setIs_selected(boolean isSelected) {
        this.is_selected = isSelected;
    }

    public void setIs_sold_out(boolean isSoldOut) {
        this.is_sold_out = isSoldOut;
    }
}
