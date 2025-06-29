package com.example.shop_mall_back.admin.order;

import com.example.shop_mall_back.common.domain.Order;
import com.nimbusds.openid.connect.sdk.claims.ClaimType;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "customer_claim")
public class CustomerClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private ClaimType claimType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_id"))
    private Order order;

    public enum ClaimType{
        취소, 반품, 교환
    }
}
