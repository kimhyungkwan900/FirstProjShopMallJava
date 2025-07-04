package com.example.shop_mall_back.user.Order.controller;

import com.example.shop_mall_back.user.Order.dto.OrderDto;
import com.example.shop_mall_back.user.Order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 주문 관련 요청을 처리하는 컨트롤러
 * - 주문 생성
 * - 결제 성공/실패 처리
 * - 배송 요청사항 저장
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService; // 주문 비즈니스 로직을 처리하는 서비스

    /**
     * [1] 주문 생성 요청
     * POST /api/orders/{memberId}
     *
     * @param memberId 주문자 회원 ID (PathVariable로 전달)
     * @param orderDto 주문 정보 (RequestBody로 전달)
     * @return 생성된 주문 ID 반환
     */
    @PostMapping("/{memberId}")
    public ResponseEntity<Long> createOrder(@PathVariable Long memberId, @RequestBody OrderDto orderDto) {
        Long orderId = orderService.createOrder(memberId, orderDto);
        return ResponseEntity.ok(orderId);
    }

    /**
     * [2] 결제 성공 처리
     * POST /api/orders/{orderId}/complete
     *
     * @param orderId 결제 완료 처리할 주문 ID
     * @return 처리 완료 메시지
     */
    @PostMapping("/{orderId}/complete")
    public ResponseEntity<String> completeOrder(@PathVariable Long orderId) {
        orderService.completePayAndOrder(orderId);
        return ResponseEntity.ok("결제 완료, 주문 접수 되었습니다.");
    }

    /**
     * [3] 결제 실패 처리
     * POST /api/orders/{orderId}/failed
     *
     * @param orderId 결제 실패 처리할 주문 ID
     * @return 처리 완료 메시지
     */
    @PostMapping("/{orderId}/failed")
    public ResponseEntity<String> failedOrder(@PathVariable Long orderId) {
        orderService.cancelPay(orderId);
        return ResponseEntity.ok("결제 실패 처리되었습니다.");
    }

    /**
     * [4] 배송 요청사항 저장
     * POST /api/orders/deliveryRequest
     *
     * @param orderDto 주문 정보 (요청사항 포함)
     * @return 생성된 주문 ID 반환
     *
     * 요청사항 유효성 검증 및 저장 처리
     */
    @PostMapping("/deliveryRequest")
    public ResponseEntity<Long> deliveryRequest(@RequestBody OrderDto orderDto) {
        // 1. 주문 생성
        Long orderId = orderService.createOrder(orderDto.getMember_id(), orderDto);

        // 2. 배송 요청사항 저장
        orderService.saveDeliveryRequestNote(orderId, orderDto);

        return ResponseEntity.ok(orderId);
    }
}
