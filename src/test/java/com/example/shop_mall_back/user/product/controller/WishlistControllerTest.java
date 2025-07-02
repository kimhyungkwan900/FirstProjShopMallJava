package com.example.shop_mall_back.user.product.controller;

import com.example.shop_mall_back.user.product.dto.WishlistItemDto;
import com.example.shop_mall_back.user.product.dto.WishlistToggleRequest;
import com.example.shop_mall_back.user.product.service.WishlistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(WishlistController.class)
class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishlistService wishlistService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/wishlist/user/{id} - 사용자 위시리스트 조회")
    void getWishlistByUserId_ShouldReturnWishlist() throws Exception {
        List<WishlistItemDto> wishlist = List.of(
                WishlistItemDto.builder()
                        .productId(100L)
                        .productName("테스트 상품")
                        .build()
        );

        when(wishlistService.getWishlistByUserId(1L)).thenReturn(wishlist);

        mockMvc.perform(get("/api/wishlist/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(100L))
                .andExpect(jsonPath("$[0].productName").value("테스트 상품"));
    }

    @Test
    @DisplayName("POST /api/wishlist/toggle - 위시리스트 토글")
    void toggleWishlist_ShouldReturnOk() throws Exception {
        WishlistToggleRequest request = new WishlistToggleRequest();
        request.setUserId(1L);
        request.setProductId(100L);

        doNothing().when(wishlistService).toggleWishlist(1L, 100L);

        mockMvc.perform(post("/api/wishlist/toggle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
