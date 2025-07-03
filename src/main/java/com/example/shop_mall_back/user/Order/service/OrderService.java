package com.example.shop_mall_back.user.Order.service;

import com.example.shop_mall_back.admin.order.domain.OrderManage;
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

    /**
     * 주문 생성 메서드
     *
     * @param memberId  주문자 ID
     * @param orderDto  주문 정보 (DTO)
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
        order.setPaymentStatus(PaymentStatus.결제대기);
        order.setTotalAmount(totalAmount);
        order.setTotalCount(totalCount);
        orderRepository.save(order);  // 변경 사항 반영

        // 7. 장바구니 항목 삭제 (주문 완료 후 비움)
        cartItemRepository.deleteAll(cartItemList);

        return order.getId();  // 주문 ID 반환
    }

    /**
     * 결제 완료 후 결제 상태 및 주문 상태를 갱신하는 메서드
     * @param orderId 주문 ID
     */
    public void completePayAndOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        // 결제 상태 변경
        order.setPaymentStatus(PaymentStatus.결제성공);

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
     * @param orderId 주문 ID
     */
    public void cancelPay(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
        order.setPaymentStatus(PaymentStatus.결제실패);
        orderRepository.save(order);
    }
}

