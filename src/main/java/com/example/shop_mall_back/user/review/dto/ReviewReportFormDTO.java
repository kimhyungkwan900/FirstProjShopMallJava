package com.example.shop_mall_back.user.review.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewReportFormDTO {
    private Long id;
    private Long reviewId;
    private Long memberId;
    private String reason;
    private String detail;
    private LocalDateTime createdAt;
}
