package com.example.shop_mall_back.common.domain;

import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberAddress;
import com.example.shop_mall_back.admin.order.domain.OrderManage;
import com.example.shop_mall_back.user.Order.constant.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor

public class Order {

    // 주문 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주문자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_order_member_id"))
    private Member member;

    // 배송지 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id", foreignKey = @ForeignKey(name = "fk_delivery_address_id"))
    private MemberAddress memberAddress;    //나중에 수정

    // 주문 일시
    @Column(name = "order_date", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime orderDate = LocalDateTime.now();

    //총 주문 결제 금액
    @Column(name = "total_amount")
    private Integer totalAmount;

    //총 주문 개수
    @Column(name = "total_count")
    private Integer totalCount;

    //결제 수단
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    // 배송 요청 사항
    @Column(name = "delivery_request")
    private String deliveryRequest;


    //주문 상태 및 처리 이력
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderManage orderManage;


}
