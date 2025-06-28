package com.example.shop_mall_back.user.product.repository;

import com.example.shop_mall_back.user.product.domain.WishlistItem;
import com.example.shop_mall_back.user.product.domain.WishlistItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, WishlistItemId> {
    /**
     * 특정 사용자(userId)의 위시리스트 항목 전체 조회
     * - 마이페이지 등에서 사용자가 찜한 상품 목록을 보여줄 때 사용
     * - 예: SELECT * FROM wishlist_item WHERE user_id = ?
     */
    List<WishlistItem> findByUserId(Long userId);
}
