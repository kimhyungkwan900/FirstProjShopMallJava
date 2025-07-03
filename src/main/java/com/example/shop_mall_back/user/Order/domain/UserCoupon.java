package com.example.shop_mall_back.user.Order.domain;

import com.example.shop_mall_back.admin.Coupon.domain.Coupon;
import com.example.shop_mall_back.common.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "member_coupon")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 사용자 쿠폰 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_usercoupon_user_id"))
    private Member member;  // 사용자 (members 테이블 참조)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", foreignKey = @ForeignKey(name = "fk_usercoupon_coupon_id"))
    private Coupon coupon;  // 쿠폰 (coupon 테이블 참조)

    @Builder.Default
    @Column(name = "is_used", nullable = false)
    private Boolean isUsed = false;  // 사용 여부

    @Column(name = "used_at")
    private LocalDateTime usedAt;  // 사용 시각 (nullable)
}

