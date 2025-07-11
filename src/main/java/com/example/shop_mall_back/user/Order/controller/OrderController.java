package com.example.shop_mall_back.user.Order.controller;

import com.example.shop_mall_back.user.Order.dto.OrderDto;
import com.example.shop_mall_back.user.Order.dto.OrderSummaryDto;
import com.example.shop_mall_back.user.Order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<OrderSummaryDto> createOrder(@PathVariable Long memberId, @RequestBody OrderDto orderDto) {
        OrderSummaryDto orderSummaryDto = orderService.createOrder(memberId, orderDto);
        return ResponseEntity.ok(orderSummaryDto);
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
    public ResponseEntity<OrderSummaryDto> deliveryRequest(@RequestBody OrderDto orderDto) {
        // 1. 주문 생성
        OrderSummaryDto orderSummaryDto = orderService.createOrder(orderDto.getMember_id(), orderDto);

        // 2. 배송 요청사항 저장
        orderService.saveDeliveryRequestNote(orderSummaryDto.getOrderId(), orderDto);

        return ResponseEntity.ok(orderSummaryDto);
    }

    /**
     * [5] 결제 처리
     * POST /api/orders/{orderId}/pay
     *
     * @param orderId 결제할 주문의 ID (PathVariable로 전달됨)
     * @param paymentToken 결제 인증 토큰 (RequestParam으로 전달됨)
     * @return 결제 결과에 따른 HTTP 응답
     *         - 성공 시: 200 OK와 메시지 반환
     *         - 실패 시: 400 Bad Request 또는 401 Unauthorized 반환
     */
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<String> pay(@PathVariable Long orderId, @RequestParam String paymentToken) {
        try {
            // 주문 결제 처리 서비스 호출
            orderService.handlePayment(orderId, paymentToken);
            return ResponseEntity.ok("결제가 성공적으로 처리되었습니다.");
        } catch (IllegalStateException e) {
            // 주문 상태나 결제 로직 문제로 발생한 예외 처리
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SecurityException e) {
            // 결제 인증 실패 시 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 결제 요청입니다.");
        }
    }

}
