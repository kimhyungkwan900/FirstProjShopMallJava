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
import com.example.shop_mall_back.user.product.domain.ProductImage;
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
     */
    @Transactional
    public void addCartItem(Long memberId, Long productId, int quantity) {
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
        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartAndProduct(cart, product);

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
        return cartItems.stream().map(item -> {
            CartItemDto dto = new CartItemDto();
            dto.setId(item.getId());
            dto.setCart_id(item.getCart().getId());
            dto.setProduct_id(item.getProduct().getId());
            dto.setQuantity(item.getQuantity());
            dto.setIs_selected(Boolean.TRUE.equals(item.getIsSelected()));
            dto.setIs_sold_out(Boolean.TRUE.equals(item.getIsSoldOut()));

            // ✅ 대표 이미지 URL 세팅
            String imageUrl = item.getProduct().getImages().stream()
                    .filter(ProductImage::isRepImg)
                    .findFirst()
                    .map(ProductImage::getImgUrl)
                    .orElse("/images/no-image.png");
            dto.setImageUrl(imageUrl);

            // ✅ 브랜드 이름 세팅
            dto.setBrandName(item.getProduct().getBrand().getName());

            // ✅ 상품 이름 세팅
            dto.setProductTitle(item.getProduct().getName());

            // ✅ 상품 가격 세팅 (원화 표시까지)
            dto.setProductPrice(String.format("%,d", item.getProduct().getPrice()));

            return dto;
        }).toList();
    }

    /**
     * [4] 장바구니 항목의 수량을 수정
     */
    public void updateCartItemOption(Long memberId, Long cartItemId, int updateQuantity) {
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
        int deliveryFee = 0;
        if (rule != null) {
            deliveryFee = (itemTotal >= rule.getMinOrderAmount()) ? 0 : rule.getDeliveryFee();
        }

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

    /**
     * [10] 위시리스트
     */

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

    /**
     * [11] 전체 선택 체크
     */
    @Transactional
    public void selectAll(Long memberId, boolean isSelected) {
        // 1. 해당 회원의 장바구니 가져오기
        Cart cart = cartRepository.findByMember_Id(memberId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다."));

        // 2. 장바구니에 담긴 모든 상품 가져오기
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        // 3. 모든 항목의 선택 상태 일괄 업데이트
        for (CartItem item : cartItems) {
            item.setIsSelected(isSelected);
        }

        cartItemRepository.saveAll(cartItems);
    }



    public void selectAllByBrand(Long currentMemberId, String brandName, boolean isSelected) {
        //1, 해당 회원의 장바구니 가져오기
        Cart cart = cartRepository.findByMember_Id((currentMemberId))
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다."));

        //2. 해당 브랜드의 모든 상품 가져오기
        List<CartItem> brandList = cartItemRepository.findByCartAndProduct_Brand_Name(cart, brandName);

        //3. 모든 항목의 선택 상태 업데이트
        for(CartItem item : brandList) {
            item.setIsSelected(isSelected);
        }
        cartItemRepository.saveAll(brandList);
    }
}

