package com.example.shop_mall_back.user.product.repository;

import com.example.shop_mall_back.user.product.domain.WishlistItem;
import com.example.shop_mall_back.user.product.domain.WishlistItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, WishlistItemId> {
    /**
     * 특정 사용자(userId)의 위시리스트 항목 전체 조회
     * - 마이페이지 등에서 사용자가 찜한 상품 목록을 보여줄 때 사용
     * - 예: SELECT * FROM wishlist_item WHERE user_id = ?
     */
    List<WishlistItem> findByUserId(Long userId);

    Optional<WishlistItem> findByUserIdAndProductId(Long userId, Long productId);

    /**
     * 특정 사용자가 특정 상품에 대해 위시리스트 또는 알림 등을 이미 등록했는지 확인하는 메서드
     * - 중복 등록 방지 용도로 사용됨
     */
    boolean existsByUserIdAndProductId(Long userId, Long productId);

}
