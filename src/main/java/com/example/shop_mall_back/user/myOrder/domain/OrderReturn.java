package com.example.shop_mall_back.user.myOrder.domain;

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
    @Enumerated(EnumType.STRING)
    private ReturnType returnType;

    @Column(name = "reason")
    private String reason;

    @Column(name = "detail")
    private String detail;

    @Column(name = "returned_at", nullable = false, updatable = false)
    private LocalDateTime regDate = LocalDateTime.now();

    public enum ReturnType {
        CANCEL_REQUEST,    // 취소 신청
        CANCEL_COMPLETE,   // 취소 완료
        RETURN_REQUEST,    // 반품 신청
        RETURN_COMPLETE,   // 반품 완료
        EXCHANGE_REQUEST,  // 교환 신청
        EXCHANGE_COMPLETE  // 교환 완료
    }
}
