package com.example.shop_mall_back.user.product.service;

import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.product.domain.WishlistItem;
import com.example.shop_mall_back.user.product.dto.WishlistItemDto;
import com.example.shop_mall_back.user.product.repository.WishlistItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistItemRepository wishlistItemRepository;

    /**
     * 특정 사용자 ID에 해당하는 찜 목록(위시리스트) 조회
     * - Repository에서 엔티티를 조회한 뒤 DTO로 변환하여 반환
     * @param userId 사용자 ID
     * @return 해당 사용자의 WishlistItemDto 리스트
     */
    public List<WishlistItemDto> getWishlistByUserId(Long userId) {
        return wishlistItemRepository.findByUserId(userId).stream()   // 사용자 ID로 찜 목록 조회
                .map(WishlistItemDto::from)                           // 엔티티를 DTO로 변환
                .collect(Collectors.toList());                        // 리스트로 수집 후 반환
    }

    public void toggleWishlist(Long userId, Long productId) {
        Optional<WishlistItem> existing = wishlistItemRepository.findByUserIdAndProductId(userId, productId);

        if (existing.isPresent()) {
            wishlistItemRepository.delete(existing.get()); // 이미 찜한 경우 제거
        } else {
            WishlistItem item = WishlistItem.builder()
                    .user(User.builder().id(userId).build()) // 더미 user 객체로 참조만
                    .product(Product.builder().id(productId).build()) // 더미 product 객체로 참조만
                    .build();
            wishlistItemRepository.save(item); // 새로운 찜 추가
        }
    }
}
