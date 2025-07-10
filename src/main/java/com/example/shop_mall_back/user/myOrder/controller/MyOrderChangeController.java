package com.example.shop_mall_back.user.myOrder.controller;

import com.example.shop_mall_back.user.myOrder.domain.OrderReturn;
import com.example.shop_mall_back.user.myOrder.dto.OrderChangeHistoryDTO;
import com.example.shop_mall_back.user.myOrder.service.MyOrderChangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MyOrderChangeController {

    private final MyOrderChangeService myOrderChangeService;

    @GetMapping("/mypage/order/changeList")
    public Page<OrderChangeHistoryDTO> getOrderChangeHistory(
            @RequestParam Long memberId,
            @RequestParam(required = false) OrderReturn.ReturnType returnType,
            @PageableDefault(size = 5, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<OrderChangeHistoryDTO> page = myOrderChangeService.getOrderChangeHistory(memberId, returnType, pageable);
        return page;
    }
}
