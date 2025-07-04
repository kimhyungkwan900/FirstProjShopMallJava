package com.example.shop_mall_back.user.Cart.service;

import com.example.shop_mall_back.common.domain.Cart;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.common.repository.MemberRepository;
import com.example.shop_mall_back.user.Cart.domain.CartItem;
import com.example.shop_mall_back.user.Cart.domain.DeliveryFeeRule;
import com.example.shop_mall_back.user.Cart.dto.CartItemDto;
import com.example.shop_mall_back.user.Cart.repository.CartItemRepository;
import com.example.shop_mall_back.user.Cart.repository.DeliveryFeeRuleRepository;
import com.example.shop_mall_back.user.product.repository.ProductRepository;
import com.example.shop_mall_back.user.Cart.repository.CartRepository;
import com.example.shop_mall_back.user.product.service.WishlistService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 장바구니 서비스 클래스
 * 사용자의 장바구니에 상품을 추가, 수정, 삭제, 조회하는 핵심 로직을 처리한다.
 */
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;                 // 장바구니 레포지토리
    private final MemberRepository memberRepository;             // 회원 레포지토리
    private final ProductRepository productRepository;           // 상품 레포지토리
    private final CartItemRepository cartItemRepository;         // 장바구니 항목 레포지토리
    private final InventoryService inventoryService;             // 재고 확인 서비스
    private final DeliveryFeeRuleRepository deliveryFeeRuleRepository;  //배송비 정책 레포지토리
    private final WishlistService wishlistService;               //위시리스트 서비스
    private final RestockAlarmService restockAlarmService;

    /**
     * [1] 장바구니에 상품을 추가하는 메서드
     *
     * @param memberId         장바구니에 상품을 담을 사용자 ID
     * @param productId        담을 상품 ID
     * @param quantity         담을 수량 (1 이상)
     * @param selectedOption   사용자가 선택한 옵션 (ex. 사이즈, 색상 등)
     */
    @Transactional
    public void addCartItem(Long memberId, Long productId, int quantity, String selectedOption) {
        //0. 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("유효한 상품이 아닙니다."));

        // 1. 수량 검증
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1이상이어야 합니다.");
        }

        // 2. 품절 여부 확인
        //품절 여부 판단
        boolean isSoldOut = inventoryService.isSoldOut(productId);

        if (isSoldOut || product.getSellStatus().equals("품절")) {
            restockAlarmService.requestRestockAlarm(memberId, productId);
            throw new IllegalStateException("해당 상품은 품절 상태로 장바구니에 담을 수 없습니다.");
        }

        // 3. 재고 수량 확인
        if (!inventoryService.isStockEnough(productId, quantity)) {
            throw new IllegalArgumentException("요청 수량이 재고보다 많습니다.");
        }

        // 4. 사용자의 장바구니 조회 (없으면 생성)
        Cart cart = cartRepository.findByMember_Id(memberId)
                .orElseGet(() -> {
                    Member member = memberRepository.findById(memberId)
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
                    Cart newCart = new Cart();
                    newCart.setMember(member);
                    return cartRepository.save(newCart);
                });

        // 5. 같은 상품 + 옵션이 이미 장바구니에 있는지 확인
        product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        //상품 존재하는지 확인
        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartAndProductAndSelectedOption(cart, product, selectedOption);

        if (existingItemOpt.isPresent()) {
            // 이미 있다면 수량만 증가
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            // 없다면 새로 생성
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setSelectedOption(selectedOption);
            cartItem.setIsSelected(true);   // 기본값: 선택된 상태
            cartItem.setIsSoldOut(false);   // 기본값: 품절 아님
            cartItemRepository.save(cartItem);
        }
    }

    /**
     * [2] 장바구니에서 특정 항목을 삭제하는 메서드
     */
    public void deleteCartItem(Long memberId, Long cartItemId) {
        // 1. 장바구니 항목 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 장바구니 항목이 존재하지 않습니다."));

        // 2. 해당 사용자의 장바구니에 속한 항목인지 검증
        if (!cartItem.getCart().getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("해당 장바구니 항목을 삭제할 권한이 없습니다.");
        }

        // 3. 삭제
        cartItemRepository.delete(cartItem);
    }

    /**
     * [3] 사용자의 장바구니 전체 항목 조회
     *
     * @return 장바구니 항목 목록 (DTO로 반환)
     */
    public List<CartItemDto> getCartItems(Long memberId) {
        Cart cart = cartRepository.findByMember_Id(memberId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다."));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        // Entity → DTO 변환
        return cartItems.stream().map(items -> {
            CartItemDto dto = new CartItemDto();
            dto.setId(items.getId());
            dto.setCart_id(items.getCart().getId());
            dto.setProduct_id(items.getProduct().getId());
            dto.setQuantity(items.getQuantity());
            dto.setSelected_option(items.getSelectedOption());
            dto.setIs_selected(items.getIsSelected() != null && items.getIsSelected());
            dto.setIs_sold_out(items.getIsSoldOut() != null && items.getIsSoldOut());
            return dto;
        }).toList();
    }

    /**
     * [4] 장바구니 항목의 수량 및 옵션을 수정
     */
    public void updateCartItemOption(Long memberId, Long cartItemId, int updateQuantity, String updateSelectedOption) {
        if (updateQuantity <= 0) {
            throw new IllegalArgumentException("수량은 1개 이상이어야 합니다.");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));

        if (!cartItem.getCart().getMember().getId().equals(memberId)) {
            throw new SecurityException("해당 장바구니 항목을 수정할 권한이 없습니다.");
        }

        if (!inventoryService.isStockEnough(cartItem.getProduct().getId(), updateQuantity)) {
            throw new IllegalArgumentException("요청한 수량이 재고보다 많습니다.");
        }

        cartItem.setQuantity(updateQuantity);
        cartItem.setSelectedOption(updateSelectedOption);
        cartItemRepository.save(cartItem);
    }

    /**
     * [5] 장바구니 항목의 선택 상태 토글
     * isSelected 값에 따라 선택/해제를 수행
     */
    public void toggleCartItemSelection(Long memberId, Long cartItemId, boolean isSelected) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));

        if (!cartItem.getCart().getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("해당 항목을 수정할 권한이 없습니다.");
        }

        if (Boolean.TRUE.equals(cartItem.getIsSoldOut()) && isSelected) {
            restockAlarmService.requestRestockAlarm(memberId, cartItem.getProduct().getId());
            throw new IllegalArgumentException("품절된 상품은 선택할 수 없습니다.");
        }

        cartItem.setIsSelected(isSelected);
        cartItemRepository.save(cartItem);
    }

    /**
     * [6] 장바구니 전체 비우기 (모든 항목 삭제)
     */
    @Transactional
    public void deleteAllCart(Long memberId) {
        cartItemRepository.deleteByCart_Member_Id(memberId);
    }

    /**
     * [7] 선택된 항목만 삭제
     */
    @Transactional
    public void delectSelectedItems(Long memberId) {
        cartItemRepository.deleteByCart_Member_IdAndIsSelectedTrue(memberId);
    }

    /**
     * [8] 사용자 장바구니 총 금액 + 배송비 계산
     */
    @Transactional
    public int calculateTotalWithDeliveryDetails(Long memberId) {
        // 1. 장바구니 선택 항목 총액 계산
        Integer result = cartItemRepository.calculateSelectedTotalAmount(memberId);
        int itemTotal = (result != null) ? result : 0;

        // 2. 배송비 정책 가져오기 (정책이 하나만 있다고 가정)
        DeliveryFeeRule rule = (DeliveryFeeRule) deliveryFeeRuleRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new IllegalStateException("배송비 정책이 존재하지 않습니다."));
        // 3. 배송비 적용
        int deliveryFee = (itemTotal >= rule.getMinOrderAmount()) ? 0 : rule.getDeliveryFee();

        // 4. 총 금액 = 상품 총액 + 배송비
        return itemTotal + deliveryFee;
    }

    /**
     * [9] 품절 여부 확인 및 반영 + 선택 해제
     * - 재고가 0인 경우 isSoldOut = true, isSelected = false 로 설정
     */
    @Transactional
    public void updateSoldOutStatusAndUnselect(Long memberId) {
        Cart cart = cartRepository.findByMember_Id(memberId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다."));

        List<CartItem> cartItemList = cartItemRepository.findByCart(cart);

        for (CartItem item : cartItemList) {
            Product product = item.getProduct();
            if (product.getStock() <= 0) {
                item.setIsSelected(false); // 품절이므로 선택 해제
                item.setIsSoldOut(true);  // 품절 처리
                restockAlarmService.requestRestockAlarm(memberId, product.getId());
            } else {
                item.setIsSelected(true);
                item.setIsSoldOut(false);
            }
            cartItemRepository.save(item);
        }
    }

    //[10] 위시리스트

    @Transactional
    public void addCartToWishlist(Long memberId, Long cartItemId) {

        //1. 장바구니 항목 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(()->new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));

        //2. 본인 소유인지 확인
        if(!cartItem.getCart().getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("해당 장바구니 항목을 이동할 권한이 없습니다");
        }

        //3. 위시리스트 중복 체크 후 없으면 추가
        Long productId = cartItem.getProduct().getId();
        if(!wishlistService.existingByUserAndProduct(memberId, productId)) {
            wishlistService.addToWishlist(memberId, productId);
        }
    }

}

