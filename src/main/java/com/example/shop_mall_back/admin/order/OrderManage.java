package com.example.shop_mall_back.admin.order;

import com.example.shop_mall_back.common.domain.Member;
import com.example.shop_mall_back.common.domain.Order;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "order_manage")
public class OrderManage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false, name = "order_status")
    private OrderStatus orderStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_id"))
    private Order order;

    public enum OrderStatus{
        접수, 확인, 배송중, 배송완료
    }
}