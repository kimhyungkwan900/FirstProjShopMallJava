package com.example.shop_mall_back.user.review.domain;

import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.user.review.domain.enums.ReviewStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_review")
@Getter
@Setter
@ToString
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "review_score", nullable = false)
    private int reviewScore; // 리뷰 점수
    @Column(name = "review_summation")
    private String reviewSummation; // 리뷰 한줄 요약
    @Column(name = "review_content")
    private String reviewContent; // 리뷰 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "order_id")
    private Long orderId; // FK 주문 ID
    @Enumerated(EnumType.STRING)
    @Column(name = "review_status", nullable = false)
    @ColumnDefault("'normal'")
    private ReviewStatus reviewStatus;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
