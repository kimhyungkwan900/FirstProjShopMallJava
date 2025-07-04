package com.example.shop_mall_back.admin.order.service;

import com.example.shop_mall_back.admin.order.domain.OrderManage;
import com.example.shop_mall_back.admin.order.dto.OrderManageDto;
import com.example.shop_mall_back.admin.order.dto.OrderSearchDto;
import com.example.shop_mall_back.admin.order.repository.OrderManageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderManageService {

    private final OrderManageRepository orderManageRepository;
    private final ModelMapper modelMapper;

    //검색 조건에 따라 고객 주문 조회
    //admin/orders, admin/orders{page}
    @Transactional(readOnly = true)
    public Page<OrderManageDto> getOrderInfoPage(OrderSearchDto orderSearchDto, Pageable pageable) {
        return orderManageRepository.getOrderPageByCondition(orderSearchDto, pageable)
                .map(orderManage->modelMapper.map(orderManage, OrderManageDto.class));
    }

    //고객 주문상태 처리
    public void updateOrderStatus(OrderManageDto orderManageDto){
        //프론트에서 받아온 OrderManage의 orderManageId로 OrderManage 테이블 탐색
        OrderManage orderManage = orderManageRepository.findById(orderManageDto.getOrderManageId())
                .orElseThrow(EntityNotFoundException::new);

        orderManage.setOrderStatus(orderManageDto.getOrderStatus());
    }

}
