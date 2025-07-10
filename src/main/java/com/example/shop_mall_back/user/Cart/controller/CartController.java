package com.example.shop_mall_back.user.Cart.controller;

import com.example.shop_mall_back.common.config.CustomUserPrincipal;
import com.example.shop_mall_back.user.Cart.dto.CartItemDto;
import com.example.shop_mall_back.user.Cart.service.CartService;
import com.example.shop_mall_back.user.Cart.service.RestockAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final RestockAlarmService restockAlarmService;

    // ✅ 로그인 사용자 ID 가져오기 (공통 메서드)
    private Long getCurrentMemberId() {
        CustomUserPrincipal userPrincipal =
                (CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getMember().getId();
    }

    /**
     * [1] 장바구니에 상품 추가
     */
    @PostMapping("/items/{productId}")
    public ResponseEntity<String> addCartItem(@PathVariable Long productId,
                                              @RequestParam int quantity) {
        try {
            cartService.addCartItem(getCurrentMemberId(), productId, quantity);
            return ResponseEntity.ok("장바구니에 상품이 추가되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * [2] 장바구니에서 특정 항목 삭제
     */
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable Long itemId) {
        cartService.deleteCartItem(getCurrentMemberId(), itemId);
        return ResponseEntity.ok("장바구니 상품이 삭제 되었습니다");
    }

    /**
     * [3] 사용자 장바구니 항목 전체 조회
     */
    @GetMapping("/items")
    public ResponseEntity<List<CartItemDto>> getCartItems() {
        List<CartItemDto> result = cartService.getCartItems(getCurrentMemberId());
        return ResponseEntity.ok(result);
    }

    /**
     * [4] 장바구니 항목 수량 또는 옵션 수정
     */
    @PutMapping("/items/{itemId}")
    public ResponseEntity<String> updateCartItem(@PathVariable Long itemId,
                                                 @RequestParam int quantity) {
        try {
            cartService.updateCartItemOption(getCurrentMemberId(), itemId, quantity);
            return ResponseEntity.ok("장바구니 항목이 수정되었습니다.");
        } catch (IllegalArgumentException | SecurityException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * [5] 선택 여부 토글 (선택/해제)
     */
    @PatchMapping("/items/{itemId}/select")
    public ResponseEntity<String> toggleSelect(@PathVariable Long itemId,
                                               @RequestParam boolean isSelected) {
        cartService.toggleCartItemSelection(getCurrentMemberId(), itemId, isSelected);
        return ResponseEntity.ok("선택 상태가 변경 되었습니다.");
    }

    /**
     * [6] 장바구니 전체 비우기 (모든 항목 삭제)
     */
    @DeleteMapping("/items")
    public ResponseEntity<String> clearCartItem() {
        cartService.deleteAllCart(getCurrentMemberId());
        return ResponseEntity.ok("장바구니를 비웠습니다.");
    }

    /**
     * [7] 선택된 항목만 삭제
     */
    @DeleteMapping("/items/selected")
    public ResponseEntity<String> deleteSelectedItems() {
        cartService.delectSelectedItems(getCurrentMemberId());
        return ResponseEntity.ok("선택된 항목이 삭제되었습니다.");
    }

    /**
     * [8] 선택된 항목 총 가격과 배송비 계산
     */
    @GetMapping("/total-with-deli")
    public ResponseEntity<Integer> calculateTotal() {
        int result = cartService.calculateTotalWithDeliveryDetails(getCurrentMemberId());
        return ResponseEntity.ok(result);
    }

    /**
     * [9] 장바구니 품절 상태 자동 갱신
     */
    @PatchMapping("/items/refresh-stock")
    public ResponseEntity<String> refreshStock() {
        cartService.updateSoldOutStatusAndUnselect(getCurrentMemberId());
        return ResponseEntity.ok("장바구니 품절 상태가 갱신되었습니다.");
    }

    /**
     * [10] 장바구니 항목을 위시리스트로 이동하는 기능
     */
    @PostMapping("/items/wishlist/{cartItemId}")
    public ResponseEntity<String> addCartToWishlist(@PathVariable Long cartItemId) {
        cartService.addCartToWishlist(getCurrentMemberId(), cartItemId);
        return ResponseEntity.ok("해당 상품이 위시리스트로 이동되었습니다.");
    }

    /**
     * [11] 재입고 알림 신청
     */
    @PostMapping("/items/{itemsId}/restockAlarm")
    public ResponseEntity<String> restockAlarm(@PathVariable Long itemsId) {
        restockAlarmService.requestRestockAlarm(getCurrentMemberId(), itemsId);
        return ResponseEntity.ok("재입고 알림이 신청되었습니다.");
    }

    /**
     * [12] 전체 선택 체크
     */
    @PutMapping("/items/select-all")
    public ResponseEntity<?> toggleSelectAll(@RequestParam boolean isSelected) {
        cartService.selectAll(getCurrentMemberId(), isSelected);
        return ResponseEntity.ok().build();
    }



}
