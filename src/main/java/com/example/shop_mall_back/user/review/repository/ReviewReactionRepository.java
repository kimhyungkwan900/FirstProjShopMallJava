package com.example.shop_mall_back.user.review.repository;

import com.example.shop_mall_back.user.review.domain.Review;
import com.example.shop_mall_back.user.review.domain.ReviewReaction;
import com.example.shop_mall_back.user.review.domain.enums.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewReactionRepository extends JpaRepository<ReviewReaction,Long> {
    Optional<ReviewReaction> findByMemberIdAndReview(Long memberId, Review review);
    int countByReview_IdAndReaction(Long reviewId, Reaction reaction);
}
