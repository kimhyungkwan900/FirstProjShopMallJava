package com.example.shop_mall_back.user.myOrder.dto;

import com.example.shop_mall_back.admin.order.domain.OrderManage;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.Order.constant.PaymentStatus;
import com.example.shop_mall_back.user.myOrder.domain.OrderReturn;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderListDTO {
    private Long id;

    private Long memberId;

    private Long orderId;

    private OrderProductDTO product;

    private Integer totalAmount;

    private Integer totalCount;

    private LocalDateTime orderDate;

    private String paymentMethod;

    private OrderManage.OrderStatus orderStatus;

    private OrderReturn.ReturnType returnType;

    private boolean existsReview;
}
