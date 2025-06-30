package com.example.shop_mall_back.reviewTest;

import com.example.shop_mall_back.user.review.domain.enums.Reaction;
import com.example.shop_mall_back.user.review.dto.ReviewReactionDTO;
import com.example.shop_mall_back.user.review.service.ReviewReactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReviewReactionTests {
    @Autowired
    private ReviewReactionService reviewReactionService;

    @Test
    @DisplayName("좋아요/싫어요 간단 테스트")
    void testReactionToggle() {
        Long reviewId = 1L;
        Long memberId = 1L;

        // 1. 좋아요 누름
        ReviewReactionDTO like = reviewReactionService.toggleReaction(reviewId, memberId, Reaction.like);
        System.out.println("좋아요 등록: " + like);

        // 2. 다시 좋아요 누르면 반응 취소됨
        ReviewReactionDTO cancelLike = reviewReactionService.toggleReaction(reviewId, memberId, Reaction.like);
        System.out.println("좋아요 취소: " + cancelLike); // null 예상

        // 3. 싫어요 누름
        ReviewReactionDTO dislike = reviewReactionService.toggleReaction(reviewId, memberId, Reaction.dislike);
        System.out.println("싫어요 등록: " + dislike);
    }
}
