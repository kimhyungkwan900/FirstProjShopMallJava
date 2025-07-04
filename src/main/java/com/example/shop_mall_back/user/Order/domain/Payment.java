package com.example.shop_mall_back.user.Order.domain;

import com.example.shop_mall_back.common.domain.Order;
import com.example.shop_mall_back.user.Order.constant.PaymentStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue
    private Long id;

    private String paymentMethod;   // 카드, 계좌이체 등
    private int amount;             // 결제 금액
    private String paymentToken;    // 결제 인증 토큰
    private PaymentStatus status;   // PENDING, SUCCESS, FAILED

    @ManyToOne
    private Order order;            // 결제와 주문 연결
}
