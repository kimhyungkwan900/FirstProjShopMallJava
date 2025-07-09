package com.example.shop_mall_back.user.myOrder.controller;

import com.example.shop_mall_back.user.myOrder.dto.OrderListDTO;
import com.example.shop_mall_back.user.myOrder.repository.MyOrderItemRepository;
import com.example.shop_mall_back.user.myOrder.service.MyOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyOrderController {

    private final MyOrderService myOrderService;

    @GetMapping("/mypage/orderList")
    public Page<OrderListDTO> getOrderList(
            @RequestParam Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return myOrderService.findByMemberId(memberId, pageable);
    }
}
