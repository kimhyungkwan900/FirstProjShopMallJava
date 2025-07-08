package com.example.shop_mall_back.admin.review.repository;

import com.example.shop_mall_back.admin.review.domain.ReviewBlind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminReviewRepository extends JpaRepository<ReviewBlind, Long> {

    void deleteByReviewId(Long reviewId);

    ReviewBlind findTopByReviewIdOrderByBlindAtDesc(Long reviewId);


}
