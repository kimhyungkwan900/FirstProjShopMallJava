package com.example.shop_mall_back.user.product.repository;

import com.example.shop_mall_back.user.product.domain.WishlistItem;
import com.example.shop_mall_back.user.product.domain.WishlistItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, WishlistItemId> {
    List<WishlistItem> findByUserId(Long userId);
}
