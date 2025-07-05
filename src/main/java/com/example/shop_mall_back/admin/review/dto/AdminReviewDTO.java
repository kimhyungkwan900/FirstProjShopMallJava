package com.example.shop_mall_back.admin.review.dto;

import com.example.shop_mall_back.user.review.domain.enums.ReviewStatus;
import com.example.shop_mall_back.user.review.dto.ReviewDTO;
import com.example.shop_mall_back.user.review.dto.ReviewImgDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AdminReviewDTO {
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

    private List<ReviewImgDTO> reviewImgDTOList;

    private String blindReason;

    private int reportCount;
}
