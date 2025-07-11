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

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MyOrderChangeController {

    private final MyOrderChangeService myOrderChangeService;

    @GetMapping("/mypage/order/changeList")
    public Page<OrderChangeHistoryDTO> getOrderChangeHistory(
            @RequestParam Long memberId,
            @RequestParam(required = false , name = "returnTypes") List<OrderReturn.ReturnType> returnTypes,
            Pageable pageable
    ) {
        return myOrderChangeService.getOrderChangeHistory(memberId, returnTypes, pageable);
    }
}
