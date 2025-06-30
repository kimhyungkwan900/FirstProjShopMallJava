package com.example.shop_mall_back.user.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewUpdateDTO {

    private int score;

    private String content;

    private String summation;

    private LocalDateTime updatedAt;

}
