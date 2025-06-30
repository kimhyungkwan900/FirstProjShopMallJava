package com.example.shop_mall_back.user.review.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewListDTO {
    private List<ReviewDTO> reviewList;
    private double averageScore;
}
