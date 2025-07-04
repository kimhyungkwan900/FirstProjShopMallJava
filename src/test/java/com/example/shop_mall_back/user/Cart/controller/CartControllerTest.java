package com.example.shop_mall_back.user.Cart.controller;

import com.example.shop_mall_back.user.Cart.service.CartService;
import com.example.shop_mall_back.user.Cart.service.RestockAlarmService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private RestockAlarmService restockAlarmService;

    @Test
    @DisplayName("장바구니 상품 추가 - 성공")
    public void getCartItems() throws Exception {

    }
}