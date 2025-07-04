package com.example.shop_mall_back.admin.order.service;

import com.example.shop_mall_back.admin.order.domain.OrderManage;
import com.example.shop_mall_back.admin.order.dto.OrderManageDto;
import com.example.shop_mall_back.admin.order.dto.OrderSearchDto;
import com.example.shop_mall_back.admin.order.repository.OrderManageRepository;
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
    /*
    주문정보를 조회로 가져오면
    체크박스로 선택해서 orderStatus의 접수를 확인, 배송중, 배송완료 처리하는 건데

     */


}
