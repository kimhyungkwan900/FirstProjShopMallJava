package com.example.shop_mall_back.user.review.dto;


import com.example.shop_mall_back.user.review.domain.enums.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private int score;
    private String summation;
    private String reviewContent;
    private Long memberId;
    private Long productId;
    private Long orderId;
    private ReviewStatus reviewStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
