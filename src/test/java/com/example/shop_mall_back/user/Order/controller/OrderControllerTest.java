package com.example.shop_mall_back.user.Order.controller;

import com.example.shop_mall_back.common.config.jwt.TokenProvider;
import com.example.shop_mall_back.user.Cart.controller.CartController;
import com.example.shop_mall_back.user.Order.repository.OrderRepository;
import com.example.shop_mall_back.user.Order.service.OrderService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false) // 💡 시큐리티 필터 비활성화
class OrderControllerTest {

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private CartController cartController;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

}