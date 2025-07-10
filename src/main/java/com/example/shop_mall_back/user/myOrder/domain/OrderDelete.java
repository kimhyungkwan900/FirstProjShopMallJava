package com.example.shop_mall_back.user.myOrder.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "order_delete")
public class OrderDelete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;
}
