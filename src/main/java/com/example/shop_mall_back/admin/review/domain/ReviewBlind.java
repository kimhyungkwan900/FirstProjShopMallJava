package com.example.shop_mall_back.admin.review.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_blind")
@Getter
@Setter
public class ReviewBlind {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "blind_at", nullable = false)
    private LocalDateTime blindAt;
}
