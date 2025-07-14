package com.example.shop_mall_back.admin.order.controller;

import com.example.shop_mall_back.admin.order.dto.OrderManageDto;
import com.example.shop_mall_back.admin.order.dto.OrderManageListDto;
import com.example.shop_mall_back.admin.order.dto.OrderSearchDto;
import com.example.shop_mall_back.admin.order.service.OrderManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/admin")
public class OrderManageController {
    private final OrderManageService orderManageService;

    //---조회 조건과 페이지 정보를 받아서 주문 데이터 조회
    @GetMapping({"/orders", "/orders/{page}"})
    public ResponseEntity<?> ordersManage(@ModelAttribute OrderSearchDto orderSearchDto, @RequestParam(value="page", defaultValue = "0") int page){

        Pageable pageable = PageRequest.of(page, 8);
        Page<OrderManageDto> orders = orderManageService.getOrderInfoPage(orderSearchDto, pageable);

        OrderManageListDto orderManageListDto = OrderManageListDto.builder()
                .orders(orders)
                .orderSearchDto(orderSearchDto)
                .maxPage(10)
                .totalPage(orders.getTotalPages())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(orderManageListDto);
    }

    //---고객 주문상태 처리
    @PatchMapping("/orders/status")
    public ResponseEntity<?> updateOrderStatus(@ModelAttribute List<OrderManageDto> statusList){
        for(OrderManageDto orderManageDto : statusList){
            orderManageService.updateOrderStatus(orderManageDto);
        }

        return ResponseEntity.ok().build();
    }
}
