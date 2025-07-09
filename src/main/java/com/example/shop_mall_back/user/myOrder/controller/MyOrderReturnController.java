package com.example.shop_mall_back.user.myOrder.controller;


import com.example.shop_mall_back.user.myOrder.dto.OrderReturnDTO;
import com.example.shop_mall_back.user.myOrder.service.MyOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyOrderReturnController {

    private final MyOrderService myOrderService;

    @PostMapping("/mypage/order/return")
    public void orderReturn(@RequestBody OrderReturnDTO orderReturnDTO) {
        myOrderService.insertOrderReturn(orderReturnDTO);
    }

}
