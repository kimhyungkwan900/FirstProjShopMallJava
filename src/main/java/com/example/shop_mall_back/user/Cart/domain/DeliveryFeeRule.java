package com.example.shop_mall_back.user.Cart.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "delivery_fee_rule")
@Getter
@Setter
@NoArgsConstructor
public class DeliveryFeeRule {

    // 배송비 정책 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 최소 주문 금액 (이 이상이면 무료배송)
    @Column(name = "min_order_amount", nullable = false)
    private Integer minOrderAmount;

    // 배송비
    @Column(name = "delivery_fee", nullable = false)
    private Integer deliveryFee;

}
