package com.example.shop_mall_back.user.myOrder.controller;

import com.example.shop_mall_back.user.myOrder.dto.OrderListDTO;
import com.example.shop_mall_back.user.myOrder.repository.MyOrderItemRepository;
import com.example.shop_mall_back.user.myOrder.service.MyOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyOrderController {

    private final MyOrderService myOrderService;

    @GetMapping("/orderList")
    public Page<OrderListDTO> getOrderList(
            @RequestParam Long memberId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(23, 59, 59) : null;

        Pageable pageable = PageRequest.of(page, size, Sort.by("order_date").descending());
        return myOrderService.findByMemberIdAndFilterNative(memberId, keyword, startDateTime, endDateTime, pageable);
    }

    // 회원 주문 삭제 (실제 삭제 x)
    @PostMapping("/orderDelete")
    public void deleteOrder(@RequestParam Long orderId) {
        myOrderService.deleteOrder(orderId);
    }
}
