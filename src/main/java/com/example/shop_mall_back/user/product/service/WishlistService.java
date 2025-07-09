package com.example.shop_mall_back.user.product.service;

import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.common.repository.MemberRepository;
import com.example.shop_mall_back.user.product.domain.WishlistItem;
import com.example.shop_mall_back.user.product.dto.WishlistItemDto;
import com.example.shop_mall_back.user.product.repository.ProductRepository;
import com.example.shop_mall_back.user.product.repository.WishlistItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final WishlistItemRepository wishlistItemRepository;

    /*
      특정 사용자 ID에 해당하는 찜 목록(위시리스트) 조회
      - Repository에서 엔티티를 조회한 뒤 DTO로 변환하여 반환
      @param userId 사용자 ID
      @return 해당 사용자의 WishlistItemDto 리스트
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
                    .user(Member.builder().id(userId).build()) // 더미 user 객체로 참조만
                    .product(Product.builder().id(productId).build()) // 더미 product 객체로 참조만
                    .build();
            wishlistItemRepository.save(item); // 새로운 찜 추가
        }
    }

    // --- 장바구니에 필요한 기능 ---

    /**
     * 위시리스트에 특정 사용자의 특정 상품이 이미 존재하는지 확인
     * - 중복 추가 방지를 위해 사용됨
     *
     * @param userId    사용자 ID
     * @param productId 상품 ID
     * @return true: 이미 위시리스트에 있음, false: 아직 없음
     */
    public boolean existingByUserAndProduct(Long userId, Long productId) {
        return wishlistItemRepository.existsByUserIdAndProductId(userId, productId);
    }

    /**
     * 위시리스트에 상품 추가
     * - 이미 등록된 상품이면 추가하지 않고 종료
     * - 존재하지 않으면 사용자와 상품 정보를 조회한 후 새로 등록
     *
     * @param userId    사용자 ID
     * @param productId 상품 ID
     */
    public void addToWishlist(Long userId, Long productId) {
        // 이미 존재하는 경우는 무시하고 return
        if (wishlistItemRepository.existsByUserIdAndProductId(userId, productId)) {
            return;
        }

        // 사용자 조회 (없으면 예외 발생)
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 상품 조회 (없으면 예외 발생)
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("유효한 상품이 아닙니다."));

        // 위시리스트 항목 생성 및 설정
        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setUser(member);     // 사용자 설정
        wishlistItem.setProduct(product); // 상품 설정

        // 저장
        wishlistItemRepository.save(wishlistItem);
    }
}
