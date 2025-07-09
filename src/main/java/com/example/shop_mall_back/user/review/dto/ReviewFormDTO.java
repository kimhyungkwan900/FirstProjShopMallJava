package com.example.shop_mall_back.user.review.dto;

import com.example.shop_mall_back.user.review.domain.Review;
import com.example.shop_mall_back.user.review.domain.enums.ReviewStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;


import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewFormDTO {
    private Long Id;
    @NotBlank(message = "별점을 선택해주세요.")
    private int score;
    private String summation;
    private String reviewContent;
    private Long memberId;
    private Long productId;
    private Long orderId;
    private ReviewStatus reviewStatus;
    private LocalDateTime createdAt;
}
