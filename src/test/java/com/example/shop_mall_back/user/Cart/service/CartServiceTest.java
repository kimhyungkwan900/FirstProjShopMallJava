package com.example.shop_mall_back.user.Cart.service;

import com.example.shop_mall_back.admin.product.domain.DeliveryInfo;
import com.example.shop_mall_back.common.domain.Cart;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.repository.MemberRepository;
import com.example.shop_mall_back.user.Cart.domain.CartItem;
import com.example.shop_mall_back.user.Cart.domain.DeliveryFeeRule;
import com.example.shop_mall_back.user.Cart.dto.CartItemDto;
import com.example.shop_mall_back.user.Cart.repository.CartItemRepository;
import com.example.shop_mall_back.user.Cart.repository.CartRepository;
import com.example.shop_mall_back.user.Cart.repository.DeliveryFeeRuleRepository;
import com.example.shop_mall_back.user.Cart.repository.RestockAlarmRepository;
import com.example.shop_mall_back.user.product.domain.Brand;
import com.example.shop_mall_back.user.product.domain.Category;
import com.example.shop_mall_back.user.product.repository.ProductRepository;
import com.example.shop_mall_back.user.product.service.WishlistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * ✅ CartService 단위 테스트 클래스
 * - 장바구니 서비스의 비즈니스 로직 검증
 * - Mockito를 사용해 의존성을 주입하고 Mock 객체로 테스트 수행
 */
@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartService cartService; // 테스트 대상 서비스

    // 💡 Mock 객체: 의존성 주입
    @Mock private CartRepository cartRepository;                // 장바구니 저장소
    @Mock private ProductRepository productRepository;          // 상품 저장소
    @Mock private MemberRepository memberRepository;            // 회원 저장소
    @Mock private CartItemRepository cartItemRepository;        // 장바구니 항목 저장소
    @Mock private DeliveryFeeRuleRepository deliveryFeeRuleRepository; // 배송비 정책 저장소
    @Mock private WishlistService wishlistService;              // 위시리스트 서비스
    @Mock private InventoryService inventoryService;            // 재고 서비스
    @Mock private RestockAlarmService restockAlarmService;      // 재입고 알림 서비스
    @Mock private RestockAlarmRepository recoveryAlarmRepository;   //재입고 알림 저장소


    @Test
    @DisplayName("장바구니 상품 추가 테스트")
    void addCartItem() {
        // given: 테스트 데이터 준비
        Long memberId = 1L;
        Long productId = 2L;
        int quantity = 3;
        String selectedOption = "option1";

        // 상품 Mock 데이터
        Product product = Product.builder()
                .id(productId)
                .name("테스트 상품")
                .description("테스트 상품입니다.")
                .price(10000)
                .stock(100)
                .brand(Brand.builder().build())
                .sellStatus(Product.SellStatus.판매중)
                .deliveryInfo(new DeliveryInfo())
                .category(new Category())
                .build();

        // 회원 Mock 데이터
        Member member = Member.builder()
                .id(memberId)
                .email("test@example.com")
                .build();

        // 장바구니 Mock 데이터
        Cart cart = new Cart();
        cart.setId(10L);
        cart.setMember(member);

        // Mock 리턴값 정의
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(inventoryService.isStockEnough(productId, quantity)).thenReturn(true);
        when(inventoryService.isSoldOut(productId)).thenReturn(false);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // when: 장바구니 추가 호출
        cartService.addCartItem(memberId, productId, quantity);

        // then: cartRepository.save()가 호출되었는지 검증
        verify(cartRepository, times(1)).save(any(Cart.class));
    }


    @Test
    @DisplayName("장바구니 특정 항목 삭제 테스트 - 성공")
    public void deleteCartItemSuccesses() {
        // given: 테스트 데이터 준비
        Long cartItemId = 10L;
        Long memberId = 1L;

        Member member = Member.builder()
                .id(memberId)
                .email("test@example.com")
                .build();

        Cart cart = new Cart();
        cart.setId(10L);
        cart.setMember(member);

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setCart(cart);

        // Mock 리턴값 정의
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        // when: 장바구니 항목 삭제 호출
        cartService.deleteCartItem(memberId, cartItemId);

        // then: cartItemRepository.delete()가 호출되었는지 검증
        verify(cartItemRepository, times(1)).delete(cartItem);
    }


    @Test
    @DisplayName("장바구니 특정 항목 삭제 테스트 - 예외 : 권한 없음")
    public void deleteCartItem_noAuthority_exception() {
        // given: 테스트 데이터 준비
        Long cartItemId = 10L;
        Long memberId = 1L; // 요청자 ID
        Long cartOwnerId = 2L; // 다른 사용자 ID

        // 장바구니 소유자
        Member cartOwner = Member.builder()
                .id(cartOwnerId)
                .email("owner@example.com")
                .build();

        // 장바구니 및 장바구니 항목
        Cart cart = new Cart();
        cart.setId(100L);
        cart.setMember(cartOwner);

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setCart(cart);

        // Mock 설정: cartItemRepository가 이 cartItem 반환
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        // when + then: 예외 발생 검증
        assertThatThrownBy(() ->
                cartService.deleteCartItem(memberId, cartItemId)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 장바구니 항목을 삭제할 권한이 없습니다.");

        // then: delete() 메서드는 호출되지 않음
        verify(cartItemRepository, never()).delete(any(CartItem.class));
    }


    @Test
    @DisplayName("장바구니 항목 전체 목록 - 성공")
    public void getCartItems_success() {
        // given: 테스트 데이터 준비
        Long memberId = 1L;

        Cart cart = new Cart();
        cart.setId(10L);

        CartItem cartItem = new CartItem();
        cartItem.setId(100L);
        cartItem.setCart(cart);
        cartItem.setProduct(Product.builder().id(200L).build());
        cartItem.setQuantity(3);
        cartItem.setIsSoldOut(false);
        cartItem.setIsSelected(true);

        // Mock 리턴값 정의
        when(cartRepository.findByMember_Id(memberId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCart(cart)).thenReturn(List.of(cartItem));

        // when: 장바구니 조회 호출
        List<CartItemDto> cartItems = cartService.getCartItems(memberId);

        // then: 결과 검증
        assertThat(cartItems).hasSize(1);
        assertThat(cartItems.getFirst().getId()).isEqualTo(cartItem.getId());
        verify(cartRepository, times(1)).findByMember_Id(memberId);
        verify(cartItemRepository, times(1)).findByCart(cart);
    }


    @Test
    @DisplayName("장바구니 항목 전체 목록 - 예외 : 장바구니 존재")
    void getCartItems_noCart_exception() {
        // given: 회원의 장바구니가 없다고 가정
        Long memberId = 1L;
        when(cartRepository.findByMember_Id(memberId)).thenReturn(Optional.empty());

        // when + then: 예외 발생 검증
        assertThatThrownBy(() -> cartService.getCartItems(memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("장바구니가 존재하지 않습니다.");
    }


    @Test
    @DisplayName("장바구니 수량 및 옵션 수정 - 성공")
    void updateCartItemOption_success() {
        // given: 테스트 데이터 준비
        Long memberId = 1L;
        Long cartItemId = 100L;
        int updateQuantity = 5; // 수정할 수량
        String updateOption = "옵션2"; // 수정할 옵션

        Member member = Member.builder().id(memberId).build();
        Product product = Product.builder().id(200L).build();

        Cart cart = new Cart();
        cart.setMember(member);

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(3);           // 기존 수량
        cartItem.setIsSoldOut(false);      // 품절 아님
        cartItem.setIsSelected(true);      // 선택됨

        // Mock 리턴값 설정
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));
        when(inventoryService.isStockEnough(product.getId(), updateQuantity)).thenReturn(true);

        // when: 수량 및 옵션 수정 호출
        cartService.updateCartItemOption(memberId, cartItemId, updateQuantity);

        // then: 변경 사항 검증
        assertThat(cartItem.getQuantity()).isEqualTo(updateQuantity);
        verify(cartItemRepository, times(1)).save(cartItem);
    }


    @Test
    @DisplayName("장바구니 수량 및 옵션 수정_수량 - 예외 : 수량")
    void updateCartItemOption_quantity_exception() {
        // given: 회원 ID와 장바구니 항목 ID
        Long memberId = 1L;
        Long cartItemId = 100L;

        // when + then: 예외 검증
        assertThatThrownBy(() ->
                cartService.updateCartItemOption(memberId, cartItemId, 0)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량은 1개 이상이어야 합니다.");
    }


    @Test
    @DisplayName("장바구니 수량 및 옵션 - 예외 : 권한")
    void updateCartItemOption_noAuthority_exception() {
        // given: 회원 ID와 다른 소유자의 장바구니 항목
        Long memberId = 1L;
        Long cartItemId = 100L;

        Member otherMember = Member.builder().id(99L).build(); // 다른 사용자
        Cart cart = new Cart();
        cart.setMember(otherMember);

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setCart(cart);

        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        // when + then: 예외 검증
        assertThatThrownBy(() ->
                cartService.updateCartItemOption(memberId, cartItemId, 1)
        )
                .isInstanceOf(SecurityException.class)
                .hasMessage("해당 장바구니 항목을 수정할 권한이 없습니다.");
    }


    @Test
    @DisplayName("장바구니 수량 및 옵션 상품 재고 - 예외")
    void updateCartItemOption_stock_exception() {
        // given: 회원 ID와 장바구니 항목
        Long memberId = 1L;
        Long cartItemId = 100L;

        Member member = Member.builder().id(memberId).build();
        Product product = Product.builder().id(200L).build();

        Cart cart = new Cart();
        cart.setMember(member);

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setCart(cart);
        cartItem.setProduct(product);

        // Mock: 요청한 수량이 재고보다 많음
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));
        when(inventoryService.isStockEnough(product.getId(), 999)).thenReturn(false);

        // when + then: 예외 검증
        assertThatThrownBy(() ->
                cartService.updateCartItemOption(memberId, cartItemId, 999)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요청한 수량이 재고보다 많습니다.");
    }


    @Test
    @DisplayName("장바구니 항목의 선택 상태 토글 - 성공")
    void toggleCartItemSelection_success() {
        // given: 회원 ID와 장바구니 항목 준비 (품절 상태 아님)
        Long memberId = 1L;         // 테스트할 회원 ID
        Long cartItemId = 100L;     // 테스트할 장바구니 항목 ID

        // 가짜 회원 엔티티 생성
        Member member = Member.builder()
                .id(memberId)
                .build();

        // 가짜 장바구니 생성 및 회원 할당
        Cart cart = new Cart();
        cart.setMember(member);

        // 가짜 장바구니 항목 생성
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);         // 장바구니 항목 ID 설정
        cartItem.setCart(cart);             // 장바구니 연결
        cartItem.setIsSelected(false);      // 기존 상태: 선택되지 않음
        cartItem.setIsSoldOut(false);       // 품절 상태 아님

        // Mock 설정: cartItemRepository.findById 호출 시 cartItem 반환
        when(cartItemRepository.findById(cartItemId))
                .thenReturn(Optional.of(cartItem));

        // when: 선택 상태를 true로 변경
        cartService.toggleCartItemSelection(memberId, cartItemId, true);

        // then: cartItemRepository.save가 한 번 호출되었는지 검증
        verify(cartItemRepository, times(1)).save(cartItem);

        // 결과 검증: 장바구니 항목의 선택 상태가 true로 변경되었는지 확인
        assertThat(cartItem.getIsSelected()).isTrue();
    }

    @Test
    @DisplayName("장바구니 항목의 선택 상태 토글 - 실패(품절 상품 선택)& 재입고 알림")
    void toggleCartItemSelection_soldOut_exception() {
        // given: 테스트용 데이터 세팅
        // 1. 테스트에 필요한 회원 ID, 장바구니 항목 ID, 상품 ID 정의
        Long memberId = 1L;
        Long cartItemId = 100L;
        Long productId  = 200L;

        // 2. 상품 엔티티 생성 (품절 상품)
        Product product = Product.builder().id(productId).build();

        // 3. 회원 엔티티 생성
        Member member = Member.builder().id(memberId).build();

        // 4. 장바구니 엔티티와 회원 연결
        Cart cart = new Cart();
        cart.setMember(member);

        // 5. 장바구니 항목 엔티티 생성 및 설정
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);            // 장바구니 항목 ID 설정
        cartItem.setCart(cart);                // 장바구니와 연결
        cartItem.setIsSelected(false);         // 현재 선택 상태: 선택되지 않음
        cartItem.setIsSoldOut(true);           // 품절 상태로 설정
        cartItem.setProduct(product);          // 상품 연결

        // 6. 장바구니 항목 Repository의 findById 호출 시 cartItem 반환하도록 stubbing
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        // when + then: 품절된 상품을 선택하려고 할 때 예외 발생 검증
        assertThatThrownBy(() ->
                // 장바구니 항목 선택 상태를 true로 변경 시도
                //toggleCartItemSelection에 requestRestockAlarmService 추가해 품절 시 재입고 알람처리 가능하게 함
                cartService.toggleCartItemSelection(memberId, cartItemId, true)
        )
                // 예상: IllegalArgumentException 발생
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("품절된 상품은 선택할 수 없습니다.");

        // verify: 재입고 알림 서비스가 호출되었는지 검증
        verify(restockAlarmService, times(1))
                .requestRestockAlarm(memberId, productId);
    }


    @Test
    @DisplayName("장바구니 전체 비우기 테스트")
    public void deleteAllCartItems() {
        // given
        Long memberId = 1L;

        // when: 지정한 회원의 장바구니를 전체 삭제
        cartService.deleteAllCart(memberId);

        // then: 해당 회원의 장바구니 항목들이 모두 삭제되었는지 검증
        verify(cartItemRepository, times(1)).deleteByCart_Member_Id(memberId);
    }

    @Test
    @DisplayName("장바구니 선택된 항목만 삭제")
    public void deleteSelectedCartItems() {
        // given
        Long memberId = 1L;

        // when: 회원의 장바구니에서 선택된 항목만 삭제
        cartService.delectSelectedItems(memberId);

        // then: 선택된 항목만 삭제 메서드가 호출되었는지 검증
        verify(cartItemRepository, times(1)).deleteByCart_Member_IdAndIsSelectedTrue(memberId);
    }

    @Test
    @DisplayName("장바구니 총 금액 + 배송비 계산 - 성공")
    public void calculateTotalWithDeliveryDetails() {
        // given
        Long memberId = 1L;
        int itemTotal = 30000; // 선택된 상품 총액
        DeliveryFeeRule deliveryFeeRule = new DeliveryFeeRule();
        deliveryFeeRule.setDeliveryFee(3000);         // 배송비 3,000원
        deliveryFeeRule.setMinOrderAmount(50000);     // 배송비 무료 기준 50,000원

        // Mock: 상품 총액과 배송비 정책 반환
        when(cartItemRepository.calculateSelectedTotalAmount(memberId)).thenReturn(itemTotal);
        when(deliveryFeeRuleRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(deliveryFeeRule));

        // when: 총액 계산 수행
        int itemTotalPrice = cartService.calculateTotalWithDeliveryDetails(memberId);

        // then: 상품 총액 + 배송비가 계산되어 반환되는지 검증
        assertThat(itemTotalPrice).isEqualTo(itemTotal + 3000);
    }

    @Test
    @DisplayName("장바구니 총 금액 + 배송비 계산 - 예외 : 배송비 정책 없음")
    void calculateTotalWithDeliveryDetails_noDeliveryFeeRule_exception() {
        // given
        Long memberId = 1L;

        // Mock: 배송비 정책이 존재하지 않음
        when(deliveryFeeRuleRepository.findTopByOrderByIdDesc()).thenReturn(Optional.empty());

        // when + then: 예외 발생 검증
        assertThatThrownBy(() -> cartService.calculateTotalWithDeliveryDetails(memberId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("배송비 정책이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("품절 여부 확인 및 반영 + 선택 해제 - 성공 + 재입고 알림 호출")
    public void updateSoldOutStatusAndUnselect_success_withRestockAlarm() {
        // given: 테스트용 데이터 및 Mock 설정
        Long memberId = 1L;

        // 1. 회원 및 장바구니 엔티티 생성
        Member member = Member.builder().id(memberId).build();
        Cart cart = new Cart();
        cart.setMember(member);

        // 2. 상품 엔티티 생성
        Product soldOutProduct = Product.builder()
                .id(101L)
                .stock(0).build();    // 재고 0 → 품절 상품

        Product inStockProduct = Product.builder()
                .id(102L)
                .stock(10).build();   // 재고 10 → 재고 있음

        // 3. 장바구니 항목 생성 및 상품 연결
        CartItem soldOutItem = new CartItem();
        soldOutItem.setProduct(soldOutProduct); // 품절 상품 연결
        soldOutItem.setIsSelected(true);        // 초기값
        soldOutItem.setIsSoldOut(false);

        CartItem inStockItem = new CartItem();
        inStockItem.setProduct(inStockProduct); // 재고 있는 상품 연결
        inStockItem.setIsSelected(false);       // 초기값
        inStockItem.setIsSoldOut(false);

        // 4. Mock: cartRepository 및 cartItemRepository가 반환할 값 정의
        when(cartRepository.findByMember_Id(memberId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCart(cart)).thenReturn(List.of(soldOutItem, inStockItem));

        // when: 장바구니 품절 상태 갱신 및 선택 해제 메소드 호출
        cartService.updateSoldOutStatusAndUnselect(memberId);

        // then: 예상 결과 검증
        // 1) 품절 상품 → 선택 해제 및 품절 상태 true
        assertThat(soldOutItem.getIsSelected()).isFalse();
        assertThat(soldOutItem.getIsSoldOut()).isTrue();

        // 2) 재고 있는 상품 → 선택 true 및 품절 상태 false
        assertThat(inStockItem.getIsSelected()).isTrue();
        assertThat(inStockItem.getIsSoldOut()).isFalse();

        // 3) cartItemRepository의 save() 메소드가 총 2번 호출되었는지 검증
        verify(cartItemRepository, times(2)).save(any(CartItem.class));

        // 4) 품절 상품에 대해 재입고 알림 호출 검증
        verify(restockAlarmService, times(1)).requestRestockAlarm(memberId, soldOutProduct.getId());


    }

    @Test
    @DisplayName("품절 여부 확인 및 반영 + 선택 해제 - 예외 : 장바구니 없음")
    void updateSoldOutStatusAndUnselect_noCart_exception() {
        // given
        Long memberId = 1L;

        // Mock: 장바구니가 없음
        when(cartRepository.findByMember_Id(memberId)).thenReturn(Optional.empty());

        // when + then: 장바구니가 없다는 예외 발생 검증
        assertThatThrownBy(() -> cartService.updateSoldOutStatusAndUnselect(memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("장바구니가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("위시리스트로 이동 - 성공")
    public void addCartToWishlist_success() {
        // given
        Long memberId = 1L;
        Long cartItemId = 2L;
        Product product = Product.builder().id(1L).build();
        Member member = Member.builder().id(memberId).build();
        Cart cart = new Cart();
        cart.setId(3L);
        cart.setMember(member);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setId(cartItemId);

        // Mock: 장바구니 항목 존재 및 위시리스트 중복 검사
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));
        when(wishlistService.existingByUserAndProduct(memberId, product.getId())).thenReturn(false);

        // when: 위시리스트로 이동
        cartService.addCartToWishlist(memberId, cartItemId);

        // then: 위시리스트에 추가 메서드가 호출되는지 검증
        verify(wishlistService, times(1)).addToWishlist(memberId, product.getId());
    }

    @Test
    @DisplayName("위시리스트로 이동 - 예외 : 장바구니 없음")
    public void addCartToWishlist_noCart_exception() {
        // given
        Long memberId = 1L;
        Long cartItemId = 2L;

        // Mock: 장바구니 항목이 존재하지 않음
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.empty());

        // when + then: 예외 발생 검증
        assertThatThrownBy(() -> cartService.addCartToWishlist(memberId, cartItemId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("장바구니 항목이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("위시리스트로 이동 - 예외 : 권한 없음")
    public void addCartToWishlist_exception() {
        // given
        Long memberId = 1L;
        Long cartItemId = 2L;
        Product product = Product.builder().id(4L).build();
        Member otherMember = Member.builder().id(6L).build();
        Cart cart = new Cart();
        cart.setId(3L);
        cart.setMember(otherMember);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setId(cartItemId);

        // Mock: 장바구니 항목 존재 (다른 사용자의 장바구니)
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        // when + then: 권한 없음 예외 검증
        assertThatThrownBy(() -> cartService.addCartToWishlist(memberId, cartItemId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 장바구니 항목을 이동할 권한이 없습니다");
    }

}

