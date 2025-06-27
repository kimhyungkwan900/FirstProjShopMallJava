package com.example.shop_mall_back.admin.order;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "order_manage")
public class OrderManage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;


}
