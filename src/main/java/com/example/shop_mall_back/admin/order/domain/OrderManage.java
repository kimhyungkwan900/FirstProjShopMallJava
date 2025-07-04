package com.example.shop_mall_back.admin.order.domain;

import com.example.shop_mall_back.common.domain.BaseEntity;
import com.example.shop_mall_back.common.domain.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "order_manage")
public class OrderManage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "order_status")
    private OrderStatus orderStatus;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_manage"))
    private Order order;

    public enum OrderStatus{
        접수, 확인, 배송중, 배송완료
    }

}