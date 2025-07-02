package com.example.shop_mall_back.user.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReviewUpdateDTO {

    private int score;

    private String reviewContent;

    private String summation;

    private LocalDateTime updatedAt;

    private List<Long> existingImageIds; // 프론트에서 유지할 이미지 ID

}
