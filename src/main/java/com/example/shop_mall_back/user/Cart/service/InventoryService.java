package com.example.shop_mall_back.user.Cart.service;

//상품 상태(품절 여부 확인)
//재고 수량 확인
public interface InventoryService {
    boolean isSoldOut(Long productId);
    boolean isStockEnough(Long productId, int requestQuantity);

}
