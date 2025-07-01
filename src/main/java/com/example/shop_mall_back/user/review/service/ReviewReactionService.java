package com.example.shop_mall_back.user.review.service;

import com.example.shop_mall_back.user.review.domain.Review;
import com.example.shop_mall_back.user.review.domain.ReviewReaction;
import com.example.shop_mall_back.user.review.domain.enums.Reaction;
import com.example.shop_mall_back.user.review.dto.ReviewReactionDTO;
import com.example.shop_mall_back.user.review.repository.ReviewReactionRepository;
import com.example.shop_mall_back.user.review.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewReactionService {

    private final ReviewReactionRepository reviewReactionRepository;
    private final ReviewRepository reviewRepository;

    public ReviewReactionDTO toggleReaction(Long memberId, Long reviewId, Reaction newReaction){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰 없음"));

        ReviewReaction reaction = reviewReactionRepository.findByMemberIdAndReview(memberId, review)
                .orElse(null);

        if(reaction == null) {
            reaction = ReviewReaction.builder()
                    .memberId(memberId)
                    .review(review)
                    .reaction(newReaction).build();
        }else {
            // 같은 반응이면 삭제, 아니면 업데이트
            if (reaction.getReaction() == newReaction) {
                reviewReactionRepository.delete(reaction);
                return null;
            } else {
                reaction.setReaction(newReaction);
            }
        }
        ReviewReaction saved = reviewReactionRepository.save(reaction);
        return ReviewReactionDTO.builder()
                .id(saved.getId())
                .memberId(saved.getMemberId())
                .reviewId(saved.getReview().getId())
                .reaction(saved.getReaction())
                .build();
    }

    public int findLikeCountByReviewId(Long reviewId) {
        return reviewReactionRepository.countByReview_IdAndReaction(reviewId, Reaction.like);
    }

    public int findDislikeCountByReviewId(Long reviewId) {
        return reviewReactionRepository.countByReview_IdAndReaction(reviewId, Reaction.dislike);
    }


}
