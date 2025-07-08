package com.example.shop_mall_back.user.Cart.controller;

import com.example.shop_mall_back.common.config.jwt.TokenProvider;
import com.example.shop_mall_back.user.Cart.dto.CartItemDto;
import com.example.shop_mall_back.user.Cart.service.CartService;
import com.example.shop_mall_back.user.Cart.service.RestockAlarmService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false) // 💡 시큐리티 필터 비활성화
class CartControllerTest {

    @MockBean
    private TokenProvider tokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private RestockAlarmService restockAlarmService;

    @Test
    @DisplayName("장바구니 상품 추가 - 성공")
    public void addCartItem_success() throws Exception {
        doNothing().when(cartService).addCartItem(anyLong(), anyLong(), anyInt());

        mockMvc.perform(post("/api/cart/items/{productId}",10)
                .param("memberId", "1")
                .param("quantity", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("장바구니에 상품이 추가되었습니다."));

        verify(cartService).addCartItem(1L,10L,2);
    }

    @Test
    @DisplayName("장바구니 상품 추가 - 실패 (예외 발생)")
    void addCartItem_fail() throws Exception{

        Mockito.doThrow(new IllegalArgumentException("상품의 재고가 부족합니다."))
                .when(cartService).addCartItem(anyLong(), anyLong(), anyInt());

        mockMvc.perform(post("/api/cart/items/{productId}",10)
                        .param("memberId", "1")
                        .param("quantity", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("상품의 재고가 부족합니다."));
    }

    @Test
    @DisplayName("[2] 장바구니 항목 삭제 - 성공")
    void deleteCartItem_success() throws Exception{


        mockMvc.perform(delete("/api/cart/items/{itemId}", 5L)
                        .param("memberId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("장바구니 상품이 삭제 되었습니다"));

        verify(cartService).deleteCartItem(1L,5L);

    }

    @Test
    @DisplayName("장바구니 전체 조회 - 성공")
    void getCartItems_success() throws Exception{

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(1L);
        cartItemDto.setProduct_id(100L);
        cartItemDto.setQuantity(2);
        cartItemDto.setSelected_option("옵션1");

        when(cartService.getCartItems(1L)).thenReturn(List.of(cartItemDto));

        mockMvc.perform(get("/api/cart/items")
                .param("memberId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].product_id").value(100L))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[0].selected_option").value("옵션1"));

        verify(cartService).getCartItems(1L);
    }

    @Test
    @DisplayName("[4] 장바구니 항목 수정 - 성공")
    void updateCartItem_success() throws Exception{

        mockMvc.perform(put("/api/cart/items/{itemId}", 5L)
                .param("memberId", "1")
                .param("quantity", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("장바구니 항목이 수정되었습니다."));

        verify(cartService).updateCartItemOption(1L, 5L, 2);
    }

    @Test
    @DisplayName("[5] 선택 상태 토글 - 성공")
    void toggleSelect_success() throws Exception{

        mockMvc.perform(patch("/api/cart/items/{itemId}/select", 5L)
                .param("memberId", "1")
                .param("isSelected", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string("선택 상태가 변경 되었습니다."));

        verify(cartService).toggleCartItemSelection(1L, 5L, true);

    }

    @Test
    @DisplayName("[6] 장바구니 전체 비우기 - 성공")
    void clearCartItem_success() throws Exception{
        mockMvc.perform(delete("/api/cart/items",5L)
                .param("memberId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("장바구니를 비웠습니다."));

        verify(cartService).deleteAllCart(1L);
    }

    @Test
    @DisplayName("[7] 선택된 항목 삭제 - 성공")
    void deleteSelectedItems_success() throws Exception{
        mockMvc.perform(delete("/api/cart/items/selected", 5L)
                .param("memberId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("선택된 항목이 삭제되었습니다."));

        verify(cartService).delectSelectedItems(1L);
    }

    @Test
    @DisplayName("[8] 총액 + 배송비 계산 - 성공")
    void calculateTotal_success() throws Exception{

        when(cartService.calculateTotalWithDeliveryDetails(1L)).thenReturn(50000);

        mockMvc.perform(get("/api/cart/total-with-deli")
                .param("memberId", "1"))
                .andExpect(status().isOk());

        verify(cartService).calculateTotalWithDeliveryDetails(1L);
    }

    @Test
    @DisplayName("[9] 장바구니 품절 상태 갱신 - 성공")
    void refreshStock_success() throws Exception{

        mockMvc.perform(patch("/api/cart/items/refresh-stock")
                .param("memberId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("장바구니 품절 상태가 갱신되었습니다."));

        verify(cartService).updateSoldOutStatusAndUnselect(1L);
    }
    @Test
    @DisplayName("[10] 위시리스트로 이동 - 성공")
    void addCartToWishlist_success() throws Exception{
        mockMvc.perform(post("/api/cart/items/wishlist/{cartItemId}", 3L)
                .param("memberId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("해당 상품이 위시리스트로 이동되었습니다."));

        verify(cartService).addCartToWishlist(1L, 3L);
    }
    @Test
    @DisplayName("[11] 재입고 알림 신청 - 성공")
    void restockAlarm_success() throws Exception {
        mockMvc.perform(post("/api/cart/items/{itemsId}/restockAlarm", 100L)
                        .param("memberId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("재입고 알림이 신청되었습니다."));

        verify(restockAlarmService).requestRestockAlarm(1L, 100L);
    }







































}