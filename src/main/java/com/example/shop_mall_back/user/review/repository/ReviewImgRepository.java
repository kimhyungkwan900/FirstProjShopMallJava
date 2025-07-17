package com.example.shop_mall_back.user.review.repository;

import com.example.shop_mall_back.user.review.domain.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
    List<ReviewImg> findByReviewId(Long reviewId);

    void deleteByReviewId(Long reviewId);

}
