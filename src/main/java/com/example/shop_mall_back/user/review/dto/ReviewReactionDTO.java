package com.example.shop_mall_back.user.review.dto;


import com.example.shop_mall_back.user.review.domain.enums.Reaction;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewReactionDTO {
    private Long id;
    private Long reviewId;
    private Long memberId;
    private Reaction reaction;
}
