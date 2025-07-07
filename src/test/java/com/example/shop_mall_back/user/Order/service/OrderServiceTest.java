package com.example.shop_mall_back.user.Order.service;

import com.example.shop_mall_back.admin.order.domain.OrderManage;
import com.example.shop_mall_back.admin.order.repository.OrderManageRepository;
import com.example.shop_mall_back.common.domain.Cart;
import com.example.shop_mall_back.common.domain.Order;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberAddress;
import com.example.shop_mall_back.common.repository.MemberAddressRepository;
import com.example.shop_mall_back.common.repository.MemberRepository;
import com.example.shop_mall_back.user.Cart.domain.CartItem;
import com.example.shop_mall_back.user.Cart.repository.CartItemRepository;
import com.example.shop_mall_back.user.Cart.service.CartService;
import com.example.shop_mall_back.user.Cart.service.InventoryService;
import com.example.shop_mall_back.user.Order.constant.PaymentStatus;
import com.example.shop_mall_back.user.Order.dto.OrderDto;
import com.example.shop_mall_back.user.Order.repository.OrderItemRepository;
import com.example.shop_mall_back.user.Order.repository.OrderRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito 단위 테스트 실행 설정
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService; // 테스트 대상 서비스

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberAddressRepository memberAddressRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CartService cartService;

    @Mock
    private InventoryService inventoryService;

    @Mock
    private OrderManageRepository orderManageRepository;

    private Member member;
    private MemberAddress memberAddress;
    private Product product;
    private CartItem cartItem;
    private Order order;
    private OrderDto orderDto;

    @BeforeEach
    void setUp() {
        // 테스트 공통 데이터 초기화
        member = Member.builder().id(1L).build();
        memberAddress = MemberAddress.builder().id(1L).member(member).build();
        product = Product.builder().id(1L).name("테스트 상품").price(10000).stock(10).build();

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setMember(member);

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        order = new Order();
        order.setId(1L);
        order.setMember(member);
        order.setMemberAddress(memberAddress);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(20000);
        order.setTotalCount(2);
        order.setPaymentMethod("CREDIT_CARD");
        order.setDeliveryRequest("문 앞에 두세요");

        orderDto = OrderDto.builder()
                .id(1L)
                .delivery_address_id(1L)
                .order_date(LocalDateTime.now())
                .total_amount(20000)
                .total_count(2)
                .payment_method("CREDIT_CARD")
                .delivery_request("문 앞에 두세요")
                .build();
    }

    @Test
    @DisplayName("주문 생성 - 성공")
    void createOrder_success() {
        // given: 회원, 배송지, 장바구니 설정
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberAddressRepository.findById(1L)).thenReturn(Optional.of(memberAddress));
        when(cartItemRepository.findByCartMemberId(1L)).thenReturn(List.of(cartItem));
        when(inventoryService.isSoldOut(1L)).thenReturn(false);
        when(inventoryService.isStockEnough(1L, 2)).thenReturn(true);
        when(cartService.calculateTotalWithDeliveryDetails(1L)).thenReturn(22000); // 배송비 포함
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when: 주문 생성
        Long createdOrderId = orderService.createOrder(1L, orderDto);

        // then: 저장 및 삭제 호출 검증
        verify(orderRepository, times(2)).save(any(Order.class));
        verify(orderItemRepository, times(1)).save(any());
        verify(cartItemRepository, times(1)).deleteAll(any());
        assertNotNull(createdOrderId);
    }

    @Test
    @DisplayName("주문 생성 - 실패 (회원 없음)")
    void createOrder_memberNotFound() {
        // given: 회원 미존재
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        // then: 예외 발생
        assertThatThrownBy(() -> orderService.createOrder(1L, orderDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("회원이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("주문 생성 - 실패 (배송지 없음)")
    void createOrder_addressNotFound() {
        // given: 배송지 미존재
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberAddressRepository.findById(1L)).thenReturn(Optional.empty());

        // then: 예외 발생
        assertThatThrownBy(() -> orderService.createOrder(1L, orderDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("배송지가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("주문 생성 - 실패 (장바구니 비어있음)")
    void createOrder_emptyCart() {
        // given: 장바구니 비어있음
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberAddressRepository.findById(1L)).thenReturn(Optional.of(memberAddress));
        when(cartItemRepository.findByCartMemberId(1L)).thenReturn(List.of());

        // then: 예외 발생
        assertThatThrownBy(() -> orderService.createOrder(1L, orderDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("선택된 상품이 없습니다.");
    }

    @Test
    @DisplayName("결제 완료 및 주문 상태 갱신 - 성공")
    void completePayAndOrder_success() {
        // given: 주문 존재
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // when: 결제 완료 처리
        orderService.completePayAndOrder(1L);

        // then: 결제 상태 및 저장 검증
        verify(orderRepository, times(1)).save(order);
        assertEquals(PaymentStatus.SUCCESS, order.getPaymentStatus());
    }

    @Test
    @DisplayName("결제 실패 처리 - 성공")
    void cancelPay_success() {
        // given: 주문 존재
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // when: 결제 실패 처리
        orderService.cancelPay(1L);

        // then: 결제 상태 및 저장 검증
        verify(orderRepository, times(1)).save(order);
        assertEquals(PaymentStatus.FAILED, order.getPaymentStatus());
    }

    @Test
    @DisplayName("배송 요청사항 저장 - 성공")
    void saveDeliveryRequestNote_success() {
        // given: 유효한 요청사항
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // when: 요청사항 저장
        orderService.saveDeliveryRequestNote(1L, orderDto);

        // then: 요청사항 저장 확인
        assertEquals("문 앞에 두세요", order.getDeliveryRequest());
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("배송 요청사항 저장 - 요청사항 길이 초과")
    void saveDeliveryRequestNote_tooLong() {
        // given: 100자를 초과하는 요청사항
        String longRequest = "a".repeat(101);
        orderDto.setDelivery_request(longRequest);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // then: 예외 발생
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> orderService.saveDeliveryRequestNote(1L, orderDto));
        assertEquals("100자 이내로 입력해야 합니다.", ex.getMessage());
    }

    @Test
    @DisplayName("isValidRequestNote - 유효한 입력")
    void isValidRequestNote_valid() {
        assertTrue(orderService.isValidRequestNote("배송은 빠르게 해주세요"));
        assertTrue(orderService.isValidRequestNote("1234 abc 한글"));
    }

    @Test
    @DisplayName("isValidRequestNote - 유효하지 않은 입력 (특수문자)")
    void isValidRequestNote_invalid() {
        assertFalse(orderService.isValidRequestNote("문 앞에 두세요!!!"));
    }

    @Test
    @DisplayName("processMockPayment - 결제 성공")
    void processMockPayment_success() {
        String result = orderService.processMockPayment("CREDIT_CARD", "TOKEN_ABC");
        assertEquals(PaymentStatus.SUCCESS.name(), result);
    }

    @Test
    @DisplayName("processMockPayment - 유효하지 않은 결제수단")
    void processMockPayment_invalidPaymentMethod() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> orderService.processMockPayment("PAYPAL", "TOKEN_ABC"));
        assertEquals("유효한 결제수단이 아닙니다.", ex.getMessage());
    }

    @Test
    @DisplayName("processMockPayment - 유효하지 않은 토큰")
    void processMockPayment_invalidToken() {
        SecurityException ex = assertThrows(SecurityException.class,
                () -> orderService.processMockPayment("CREDIT_CARD", "INVALID_TOKEN"));
        assertEquals("유효하지 않은 결제 토큰입니다.", ex.getMessage());
    }

    @Test
    @DisplayName("handlePayment - 결제 성공 처리")
    void handlePayment_success() {
        // given: 결제 대기 상태 주문
        order.setPaymentStatus(PaymentStatus.PENDING);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // when: 결제 처리
        orderService.handlePayment(1L, "TOKEN_VALID");

        // then: 결제 성공 및 저장 확인
        assertEquals(PaymentStatus.SUCCESS, order.getPaymentStatus());
        verify(orderRepository, times(1)).save(order);
        verify(orderManageRepository).save(any(OrderManage.class));
    }

    @Test
    @DisplayName("handlePayment - 이미 결제된 주문")
    void handlePayment_alreadyPaid() {
        // given: 이미 결제 완료된 주문
        order.setPaymentStatus(PaymentStatus.SUCCESS);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // then: 예외 발생
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> orderService.handlePayment(1L, "TOKEN_VALID"));
        assertEquals("이미 결제 처리된 주문입니다.", ex.getMessage());
    }
}

