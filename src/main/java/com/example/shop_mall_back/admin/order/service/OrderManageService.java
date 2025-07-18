package com.example.shop_mall_back.admin.order.service;

import com.example.shop_mall_back.admin.order.domain.OrderManage;
import com.example.shop_mall_back.admin.order.dto.AdminOrderDto;
import com.example.shop_mall_back.admin.order.dto.OrderManageDto;
import com.example.shop_mall_back.admin.order.dto.OrderSearchDto;
import com.example.shop_mall_back.admin.order.dto.OrderUpdateDto;
import com.example.shop_mall_back.admin.order.repository.OrderManageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderManageService {

    private final OrderManageRepository orderManageRepository;

    //검색 조건에 따라 고객 주문 조회
    //admin/orders, admin/orders{page}
    @Transactional(readOnly = true)
    public Page<OrderManageDto> getOrderInfoPage(OrderSearchDto orderSearchDto, Pageable pageable) {

        System.out.println("검색유형" + orderSearchDto.getSearchType());
        System.out.println("검색어" + orderSearchDto.getSearchContent());
        System.out.println("주문상태" + orderSearchDto.getOrderStatus());
        System.out.println("시작일" +orderSearchDto.getStartDate());
        System.out.println("종료일" + orderSearchDto.getEndDate());

        Page<OrderManage> orderInfoPage = orderManageRepository.getOrderPageByCondition(orderSearchDto, pageable);

        return orderInfoPage.map(orderManage ->
                OrderManageDto.builder()
                        .orderManageId(orderManage.getId())
                        .orderStatus(orderManage.getOrderStatus())
                        .order(
                                AdminOrderDto.builder()
                                        .id(orderManage.getOrder().getId())
                                        .member_id(orderManage.getOrder().getMember().getUserId())
                                        .delivery_address_id(orderManage.getOrder().getMemberAddress().getId())
                                        .delivery_address(orderManage.getOrder().getMemberAddress().getAddress())
                                        .order_date(orderManage.getOrder().getOrderDate())
                                        .total_amount(orderManage.getOrder().getTotalAmount())
                                        .total_count(orderManage.getOrder().getTotalCount())
                                        .payment_method(orderManage.getOrder().getPaymentMethod())
                                        .delivery_request(orderManage.getOrder().getDeliveryRequest())
                                        .build()
                        )
                        .build());
    }

    //고객 주문상태 처리
    public void updateOrderStatus(OrderUpdateDto orderUpdateDto){
        //프론트에서 받아온 OrderManage의 orderManageId로 OrderManage 테이블 탐색
        for(Long id:orderUpdateDto.getIds()){
            OrderManage orderManage = orderManageRepository.findById(id)
                    .orElseThrow(EntityNotFoundException::new);

            orderManage.setOrderStatus(orderUpdateDto.getNewStatus());
        }
    }

}
