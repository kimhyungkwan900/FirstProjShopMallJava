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
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService; // 주문 비즈니스 로직을 처리하는 서비스

    // [1] 주문 생성 요청
    @PostMapping("/{memberId}")
    public ResponseEntity<Long> createOrder(@PathVariable Long memberId, @RequestBody OrderDto orderDto) {
        Long orderId = orderService.createOrder(memberId, orderDto);
        return ResponseEntity.ok(orderId);
    }

    //[2] 결제 성공 처리
    @PostMapping("/{orderId}/complete")
    public ResponseEntity<String> completeOrder(@PathVariable Long orderId) {
        orderService.completePayAndOrder(orderId);
        return ResponseEntity.ok("결제 완료, 주문 접수 되었습니다.");
    }

    //[3] 결제 실패 처리
    @PostMapping("/{orderId}/failed")
    public ResponseEntity<String> failedOrder(@PathVariable Long orderId) {
        orderService.cancelPay(orderId);
        return ResponseEntity.ok("결제 실패 처리되었습니다.");
    }
}

