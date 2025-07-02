package com.example.shop_mall_back.user.review.dto;


import com.example.shop_mall_back.user.review.domain.Review;
import com.example.shop_mall_back.user.review.domain.enums.ReviewStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

    private List<ReviewImgDTO> reviewImgDTOList;

    private int likeCount;
    private int dislikeCount;
}
