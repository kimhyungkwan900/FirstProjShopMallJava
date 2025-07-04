package com.example.shop_mall_back.user.review.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ReviewReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "detail", nullable = false)
    private String detail;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
