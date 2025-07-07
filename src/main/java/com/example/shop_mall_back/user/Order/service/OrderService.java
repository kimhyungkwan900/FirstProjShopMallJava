package com.example.shop_mall_back.user.Order.service;

import com.example.shop_mall_back.admin.order.domain.OrderManage;
import com.example.shop_mall_back.admin.order.repository.OrderManageRepository;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberAddress;
import com.example.shop_mall_back.common.domain.Order;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.common.repository.MemberAddressRepository;
import com.example.shop_mall_back.common.repository.MemberRepository;
import com.example.shop_mall_back.user.Cart.domain.CartItem;
import com.example.shop_mall_back.user.Cart.repository.CartItemRepository;
import com.example.shop_mall_back.user.Cart.service.CartService;
import com.example.shop_mall_back.user.Cart.service.InventoryService;
import com.example.shop_mall_back.user.Order.domain.OrderItem;
import com.example.shop_mall_back.user.Order.dto.OrderDto;
import com.example.shop_mall_back.user.Order.repository.OrderItemRepository;
import com.example.shop_mall_back.user.Order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.shop_mall_back.user.Order.constant.PaymentStatus;


import java.util.List;

/**
 * 주문 처리 서비스
 * - 주문 생성, 결제 성공/실패 상태 관리 등을 담당
 * 장바구니 -> 주문 변경
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    // 의존성 주입: 필요한 리포지토리 및 서비스들
    private final MemberRepository memberRepository;
    private final MemberAddressRepository memberAddressRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final InventoryService inventoryService;
    private final OrderManageRepository orderManageRepository;

    /**
     * 주문 생성 메서드
     *
     * @param memberId 주문자 ID
     * @param orderDto 주문 정보 (DTO)
     * @return 생성된 주문 ID
     */
    public Long createOrder(Long memberId, OrderDto orderDto) {

        // 1. 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 2. 배송지 조회
        MemberAddress memberAddress = memberAddressRepository.findById(orderDto.getDelivery_address_id())
                .orElseThrow(() -> new IllegalArgumentException("배송지가 존재하지 않습니다."));

        // 3. 장바구니 항목 조회 (선택된 항목들)
        List<CartItem> cartItemList = cartItemRepository.findByCartMemberId(memberId);
        if (cartItemList.isEmpty()) {
            throw new IllegalArgumentException("선택된 상품이 없습니다.");
        }

        // 4. 주문 엔티티 생성 및 정보 설정
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setMember(member);
        order.setMemberAddress(memberAddress);
        order.setOrderDate(orderDto.getOrder_date());
        order.setTotalAmount(orderDto.getTotal_amount());
        order.setTotalCount(orderDto.getTotal_count());
        order.setPaymentMethod(orderDto.getPayment_method());
        order.setDeliveryRequest(orderDto.getDelivery_request());
        order.setIsGuest(false);  // 비회원 주문 아님
        order = orderRepository.save(order);  // 주문 저장 (ID 자동 생성됨)

        //관리자가 주문 관리할 수 있도록 order_manage 에 주문정보 저장
        OrderManage orderManage = new OrderManage();
        orderManage.setOrderStatus(OrderManage.OrderStatus.접수);
        orderManage.setOrder(order);
        orderManageRepository.save(orderManage);

        int totalAmount = 0;
        int totalCount = 0;

        // 5. 각 장바구니 항목을 주문 항목으로 전환
        for (CartItem cartItem : cartItemList) {
            Product product = cartItem.getProduct();

            // 5-1. 품절 및 재고 체크
            if (inventoryService.isSoldOut(product.getId())) {
                throw new IllegalArgumentException(product.getName() + " 해당 상품은 품절입니다.");
            }

            if (!inventoryService.isStockEnough(product.getId(), cartItem.getQuantity())) {
                throw new IllegalArgumentException(product.getName() + " 상품 재고가 부족합니다.");
            }

            // 5-2. 주문 항목 생성 및 저장
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());  // 단가
            orderItem.setSelectedOption(cartItem.getSelectedOption());
            orderItemRepository.save(orderItem);


            // 5-3. 총액 및 총수량 계산 (배송비 포함)
            totalAmount += cartService.calculateTotalWithDeliveryDetails(memberId);
            totalCount += cartItem.getQuantity();

            // 5-4. 재고 차감
            product.setStock(product.getStock() - cartItem.getQuantity());
        }

        // 6. 계산된 주문 정보 반영 및 상태 저장
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setTotalAmount(totalAmount);
        order.setTotalCount(totalCount);
        orderRepository.save(order);  // 변경 사항 반영

        // 7. 장바구니 항목 삭제 (주문 완료 후 비움)
        cartItemRepository.deleteAll(cartItemList);

        return order.getId();  // 주문 ID 반환
    }

    /**
     * 결제 완료 후 결제 상태 및 주문 상태를 갱신하는 메서드
     *
     * @param orderId 주문 ID
     */
    public void completePayAndOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        // 결제 상태 변경
        order.setPaymentStatus(PaymentStatus.SUCCESS);

        // 주문 관리 엔티티 설정 (주문 상태 관리용)
        OrderManage orderManage = order.getOrderManage();
        if (orderManage == null) {
            orderManage = new OrderManage();
            orderManage.setOrder(order);  // 양방향 연관관계 설정
            order.setOrderManage(orderManage);
        }

        // 주문 상태: 접수 처리
        orderManage.setOrderStatus(OrderManage.OrderStatus.접수);
        orderRepository.save(order);
    }

    /**
     * 결제 실패 시 결제 상태를 "결제실패"로 변경
     *
     * @param orderId 주문 ID
     */
    public void cancelPay(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
        order.setPaymentStatus(PaymentStatus.FAILED);
        orderRepository.save(order);
    }

    // 배송 요청사항 저장 메서드
    public void saveDeliveryRequestNote(Long orderId, OrderDto orderDto) {

        // 요청사항 최대 허용 길이
        int MAX_REQUEST_NOTE_LENGTH = 100;

        // 1. 주문 유효성 검사
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        // 2. 요청사항 유효성 검증
        String requestNote = orderDto.getDelivery_request();

        // (a) 요청사항이 최대 길이를 초과하면 예외 발생
        if (requestNote.length() > MAX_REQUEST_NOTE_LENGTH) {
            throw new IllegalArgumentException(MAX_REQUEST_NOTE_LENGTH + "자 이내로 입력해야 합니다.");
        }

        // (b) 요청사항에 특수문자가 포함되어 있으면 예외 발생
        if (!isValidRequestNote(requestNote)) {
            throw new IllegalArgumentException("특수문자는 작성하실 수 없습니다.");
        }

        // 3. 요청사항을 주문 엔티티에 저장하고 DB에 반영
        order.setDeliveryRequest(requestNote); // 주문 객체에 요청사항 설정
        orderRepository.save(order);           // 변경사항 저장
    }

    /**
     * 요청사항 유효성 검사 메서드
     * - null/빈 문자열 허용
     * - 한글, 영문, 숫자, 공백만 허용
     * - 특수문자 차단
     */
    public boolean isValidRequestNote(String requestNote) {
        // 요청사항이 null 또는 빈 문자열이면 유효
        if (requestNote == null || requestNote.isEmpty()) {
            return true;
        }

        // 특수문자 차단 (한글, 영문, 숫자, 공백만 허용)
        return requestNote.matches("^[가-힣a-zA-Z0-9 ]*$");
    }

    /**
     * Mock 결제 처리 메서드
     * - 결제 토큰 검증 및 허용된 결제수단 유효성 체크
     * - 유지보수를 고려하여 결제 검증과 결제 로직을 분리
     * - 실제 PG사와 연결 대신 Mock 방식으로 처리
     *
     * @param paymentMethod 결제수단 (ex: CREDIT_CARD, MOBILE_PHONE, BANK_TRANSFER)
     * @param paymentToken 결제 인증 토큰
     * @return 결제 상태 (SUCCESS 또는 FAILED)
     */
    public String processMockPayment(String paymentMethod, String paymentToken) {

        // 1. 허용된 결제수단 목록 정의
        List<String> paymentMethodList = List.of("CREDIT_CARD", "MOBILE_PHONE", "BANK_TRANSFER");

        // 2. 결제수단 유효성 검사
        if (paymentMethod == null || !paymentMethodList.contains(paymentMethod)) {
            throw new IllegalArgumentException("유효한 결제수단이 아닙니다.");
        }


        // 3. 결제 토큰 유효성 검사
        if (!validatePaymentToken(paymentToken)) {
            throw new SecurityException("유효하지 않은 결제 토큰입니다.");
        }

        // 4. Mock 결제 처리 결과 반환
        if ("CREDIT_CARD".equals(paymentMethod) || "MOBILE_PHONE".equals(paymentToken) || "BANK_TRANSFER".equals(paymentMethod)) {
            return PaymentStatus.SUCCESS.name();
        } else {
            return PaymentStatus.FAILED.name();
        }
    }

    /**
     * 결제 토큰 유효성 검증 메서드
     * - 실제 서비스라면 서명 검증, 만료 시간 체크 등을 수행
     * - Mock 환경에서는 간단히 prefix 체크로 대체
     *
     * @param paymentToken 결제 인증 토큰
     * @return 유효하면 true, 아니면 false
     */
    public boolean validatePaymentToken(String paymentToken) {
        return paymentToken != null && paymentToken.startsWith("TOKEN_");
    }

    /**
     * 결제 처리 및 주문 상태 업데이트 메서드
     * - 결제 성공 시: 주문 상태를 "접수"로 변경
     * - 결제 실패 시: 주문 상태를 "결제실패"로 변경
     *
     * @param orderId 결제할 주문 ID
     * @param paymentToken 결제 인증 토큰
     */
    public void handlePayment(Long orderId, String paymentToken) {

        // 1. 주문 유효성 검사
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        // 1-1. 이미 결제 완료된 주문인지 체크
        if (order.getPaymentStatus().equals(PaymentStatus.SUCCESS)) {
            throw new IllegalArgumentException("이미 결제 처리된 주문입니다.");
        }

        // 2. Mock 결제 처리
        String paymentStatus = processMockPayment(order.getPaymentMethod(), paymentToken);

        if (PaymentStatus.SUCCESS.name().equals(paymentStatus)) {
            // 결제 성공 처리
            order.setPaymentStatus(PaymentStatus.SUCCESS);

            // 주문 상태를 접수로 변경
            OrderManage orderManage = order.getOrderManage();
            if (orderManage == null) {
                orderManage = new OrderManage();
                orderManage.setOrder(order); // 양방향 연관관계 설정
                order.setOrderManage(orderManage);
            }
            orderManage.setOrderStatus(OrderManage.OrderStatus.접수);

            orderRepository.save(order);
            orderManageRepository.save(orderManage);
        } else {
            // 결제 실패 처리
            order.setPaymentStatus(PaymentStatus.FAILED);
            orderRepository.save(order);
        }
    }

}

