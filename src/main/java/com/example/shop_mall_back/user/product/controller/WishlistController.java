package com.example.shop_mall_back.user.product.controller;

import com.example.shop_mall_back.user.product.dto.WishlistItemDto;
import com.example.shop_mall_back.user.product.dto.WishlistToggleRequest;
import com.example.shop_mall_back.user.product.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    // 특정 사용자의 위시리스트를 조회하는 GET 요청 처리
    @GetMapping("/user/{id}")
    public List<WishlistItemDto> getWishlist(@PathVariable Long userId) {
        // 경로에서 받은 사용자의 id를 기반으로 해당 사용자의 위시리스트를 서비스에서 조회하여 반환
        return wishlistService.getWishlistByUserId(userId);
    }

    @PostMapping("/toggle")
    public void toggleWishlist(@RequestBody WishlistToggleRequest request) {
        wishlistService.toggleWishlist(request.getUserId(), request.getProductId());
    }
}