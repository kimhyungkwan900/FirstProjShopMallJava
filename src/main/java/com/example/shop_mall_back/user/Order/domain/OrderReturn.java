package com.example.shop_mall_back.user.Order.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_return")
@Getter
@Setter
public class OrderReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "return_type")
    private ReturnType returnType;

    @Column(name = "reason")
    private String reason;

    @Column(name = "detail")
    private String detail;

    @Column(name = "returned_at", nullable = false, updatable = false)
    private LocalDateTime regDate = LocalDateTime.now();

    public enum ReturnType{
        exchange, RETURN, CANCEL
    }
}
