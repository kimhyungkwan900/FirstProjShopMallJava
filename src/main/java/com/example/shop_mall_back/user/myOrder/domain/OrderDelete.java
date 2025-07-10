package com.example.shop_mall_back.user.myOrder.domain;

import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Table(name = "order_delete")
@Setter
public class OrderDelete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;
}
