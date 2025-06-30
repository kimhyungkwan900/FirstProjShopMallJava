package com.example.shop_mall_back.user.review.controller;

import com.example.shop_mall_back.user.review.domain.enums.Reaction;
import com.example.shop_mall_back.user.review.dto.ReviewReactionDTO;
import com.example.shop_mall_back.user.review.service.ReviewReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewReactionController {
    private final ReviewReactionService reviewReactionService;

    @PostMapping("/review-reactions")
    public ResponseEntity<?> react(
            @RequestParam Long memberId,
            @RequestParam Long reviewId,
            @RequestParam Reaction reaction){
     ReviewReactionDTO result = reviewReactionService.toggleReaction(memberId,reviewId,reaction);
     return ResponseEntity.ok(result);

    }
}
