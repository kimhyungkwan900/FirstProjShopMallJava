package com.example.shop_mall_back.admin.order.domain;

import com.example.shop_mall_back.user.myOrder.domain.OrderReturn;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "customer_claim")
public class ClaimManage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_id", foreignKey = @ForeignKey(name = "fk_return_id"))
    private OrderReturn orderReturn;

    @Setter
    @Column
    private Boolean isApproved;
}
