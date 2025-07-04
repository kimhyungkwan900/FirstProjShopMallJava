package com.example.shop_mall_back.admin.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminReviewBlindDTO {
    private Long id;
    private Long reviewId;
    private Long adminId;
    private String reason;
    private LocalDateTime blindAt;
}
