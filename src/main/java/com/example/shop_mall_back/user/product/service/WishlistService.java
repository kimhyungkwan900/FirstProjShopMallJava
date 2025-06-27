package com.example.shop_mall_back.user.product.service;

import com.example.shop_mall_back.user.product.dto.WishlistItemDto;
import com.example.shop_mall_back.user.product.repository.WishlistItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistItemRepository wishlistItemRepository;

    public List<WishlistItemDto> getWishlistByUserId(Long userId) {
        return wishlistItemRepository.findByUserId(userId).stream()
                .map(WishlistItemDto::from)
                .collect(Collectors.toList());
    }
}
