package com.example.shop_mall_back.user.Cart.domain;

import com.example.shop_mall_back.common.domain.Member;
import com.example.shop_mall_back.common.domain.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "restock_alarm")
@Getter
@Setter
@NoArgsConstructor

/*
 * 재입고 알림 엔티티
 * 사용자가 품절된 상품에 대해 재입고 시 알림을 받을 수 있도록 신청한 정보를 저장한다.
 */

public class RestockAlarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 알림  ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;  // 알림을 신청한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // 알림 대상 상품

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    // 신청 시각 (기본값: 현재 시간)

    @Column(name = "notified", nullable = false)
    private boolean notified = false;
    // 알림 발송 여부: false = 미발송, true = 발송 완료
}
