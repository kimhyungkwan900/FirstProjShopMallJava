package com.example.shop_mall_back.admin.product.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "delivery_info")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String delivery_yn;

    @Enumerated(EnumType.STRING)
    private Delivery_com deliveryCom;

    @Column(nullable = false, name = "delivery_price")
    private int deliveryPrice;

    public enum Delivery_com {
        CJ, LOGEN, LOTTE
    }
}
