package com.example.shop_mall_back.user.Cart.domain;

import com.example.shop_mall_back.admin.Coupon.domain.Coupon;
import com.example.shop_mall_back.common.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "member_coupon")
@Getter
@Setter
@NoArgsConstructor
public class MemberCoupon {

    // 사용자 쿠폰 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 회원 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_usercoupon_user_id"))
    private Member member;

    // 쿠폰 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_usercoupon_coupon_id"))
    private Coupon coupon;

    // 쿠폰 사용 여부
    @Column(name = "is_used")
    private Boolean isUsed = false;

    // 쿠폰 사용 시각 (nullable)
    @Column(name = "used_at")
    private LocalDateTime usedAt;
}