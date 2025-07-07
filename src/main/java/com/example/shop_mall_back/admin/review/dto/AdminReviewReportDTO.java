package com.example.shop_mall_back.admin.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminReviewReportDTO {
    private Long reviewId;
    private Long memberId;
    private String reason;
    private String detail;
    private LocalDateTime createdAt;
}
