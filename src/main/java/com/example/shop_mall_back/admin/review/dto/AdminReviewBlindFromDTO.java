package com.example.shop_mall_back.admin.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminReviewBlindFromDTO {
    private Long id;
    private long reviewId;
    private long adminId;
    private String reason;
    private LocalDateTime blindAt;
}
