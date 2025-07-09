package com.example.shop_mall_back.user.Cart.controller;

import com.example.shop_mall_back.user.Cart.dto.CartItemDto;
import com.example.shop_mall_back.user.Cart.service.CartService;
import com.example.shop_mall_back.user.Cart.service.RestockAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 장바구니 관련 요청을 처리하는 컨트롤러
 * - 상품 추가, 삭제, 수정, 조회, 선택 여부 토글 등 REST API 제공
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * [1] 장바구니에 상품 추가
     * @param memberId 사용자 ID
     * @param productId 상품 ID
     * @param quantity 수량 (1 이상)

     */
    @PostMapping("/items/{productId}")
    public ResponseEntity<String> addCartItem(@RequestParam Long memberId,
                                              @PathVariable Long productId,
                                              @RequestParam int quantity) {
        try {
            cartService.addCartItem(memberId, productId, quantity);
            return ResponseEntity.ok("장바구니에 상품이 추가되었습니다.");
        } catch (IllegalArgumentException e) {
            // 재고 부족, 품절 등의 예외 메시지를 사용자에게 전달
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * [2] 장바구니에서 특정 항목 삭제
     * @param memberId 사용자 ID
     * @param itemId 장바구니 항목 ID
     */
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<String> deleteCartItem(@RequestParam Long memberId,
                                                 @PathVariable Long itemId) {
        cartService.deleteCartItem(memberId, itemId);
        return ResponseEntity.ok("장바구니 상품이 삭제 되었습니다");
    }

    /**
     * [3] 사용자 장바구니 항목 전체 조회
     * @param memberId 사용자 ID
     * @return 장바구니 항목 목록
     *
     * 💡 현재 Response가 빈 리스트로 리턴됨 (코드 미완성)
     */
    @GetMapping("/items")
    public ResponseEntity<List<CartItemDto>> getCartItems(@RequestParam Long memberId) {
        List<CartItemDto> result = cartService.getCartItems(memberId);
        return ResponseEntity.ok(result);
    }

    /**
     * [4] 장바구니 항목 수량 또는 옵션 수정
     * @param memberId 사용자 ID
     * @param itemId 장바구니 항목 ID
     * @param quantity 수정할 수량
     */
    @PutMapping("/items/{itemId}")
    public ResponseEntity<String> updateCartItem(@RequestParam Long memberId,
                                                 @PathVariable Long itemId,
                                                 @RequestParam int quantity) {
        try {
            cartService.updateCartItemOption(memberId, itemId, quantity);
            return ResponseEntity.ok("장바구니 항목이 수정되었습니다.");
        } catch (IllegalArgumentException | SecurityException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * [5] 선택 여부 토글 (선택/해제)
     * @param memberId 사용자 ID
     * @param itemId 장바구니 항목 ID
     * @param isSelected true → 선택 / false → 선택 해제
     */
    @PatchMapping("/items/{itemId}/select")
    public ResponseEntity<String> toggleSelect(@RequestParam Long memberId,
                                               @PathVariable Long itemId,
                                               @RequestParam boolean isSelected) {
        cartService.toggleCartItemSelection(memberId, itemId, isSelected);
        return ResponseEntity.ok("선택 상태가 변경 되었습니다.");
    }

    /**
     * [6] 장바구니 전체 비우기 (모든 항목 삭제)
     * @param memberId 사용자 ID
     */
    @DeleteMapping("/items")
    public ResponseEntity<String> clearCartItem(@RequestParam Long memberId) {
        cartService.deleteAllCart(memberId);
        return ResponseEntity.ok("장바구니를 비웠습니다.");
    }

    /**
     * [7] 선택된 항목만 삭제
     * @param memberId 사용자 ID
     */
    @DeleteMapping("/items/selected")
    public ResponseEntity<String> delectSelectedItems(@RequestParam Long memberId) {
        cartService.delectSelectedItems(memberId);
        return ResponseEntity.ok("선택된 항목이 삭제되었습니다.");
    }

    /**
     * [8] 선택된 항목 총 가격과 배송비 계산
     * @param memberId 사용자 ID
     * @return 선택된 상품들과 배송비의 합의 총 금액
     */
    @GetMapping("/total-with-deli")
    public ResponseEntity<Integer> calculateTotal(@RequestParam Long memberId) {
        int result = cartService.calculateTotalWithDeliveryDetails(memberId);
        return ResponseEntity.ok(result);
    }

    /**
     * [9] 장바구니 품절 상태 자동 갱신
     * - 품절이면 isSoldOut=true, isSelected=false로 설정
     * @param memberId 사용자 ID
     */
    @PatchMapping("/items/refresh-stock")
    public ResponseEntity<String> refreshStock(@RequestParam Long memberId) {
        cartService.updateSoldOutStatusAndUnselect(memberId);
        return ResponseEntity.ok("장바구니 품절 상태가 갱신되었습니다.");
    }

    /**
     * [10] 장바구니 항목을 위시리스트로 이동하는 기능
     * POST /api/cart/items/wishlist/{cartItemId}?memberId=1
     *
     * @param memberId    사용자 ID
     * @param cartItemId  장바구니 항목 ID
     * @return 성공 메시지
     */
    @PostMapping("/items/wishlist/{cartItemId}")
    public ResponseEntity<String> addCartToWishlist(@RequestParam Long memberId,
                                                    @PathVariable Long cartItemId) {
        cartService.addCartToWishlist(memberId, cartItemId); // Cart → Wishlist 이동 로직 실행
        return ResponseEntity.ok("해당 상품이 위시리스트로 이동되었습니다.");
    }


    // RestockAlarmService 의존성 주입 (재입고 알림 신청에 사용)
    private final RestockAlarmService restockAlarmService;

    /**
     * [11] 재입고 알림 신청
     * POST /api/cart/items/{itemsId}/restockAlarm?memberId=1
     *
     * @param memberId 사용자 ID
     * @param itemsId  알림 신청할 상품 ID
     * @return 성공 메시지
     */
    @PostMapping("/items/{itemsId}/restockAlarm")
    public ResponseEntity<String> restockAlarm(@RequestParam Long memberId,
                                               @PathVariable Long itemsId) {
        restockAlarmService.requestRestockAlarm(memberId, itemsId); // 알림 신청 처리
        return ResponseEntity.ok("재입고 알림이 신청되었습니다.");
    }
}
