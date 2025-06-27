package com.example.shop_mall_back.admin.order;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "customer_claim")
public class CustomerClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
