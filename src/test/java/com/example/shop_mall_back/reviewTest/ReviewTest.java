package com.example.shop_mall_back.reviewTest;

import com.example.shop_mall_back.user.review.domain.Review;
import com.example.shop_mall_back.user.review.domain.enums.ReviewStatus;
import com.example.shop_mall_back.user.review.dto.ReviewDTO;
import com.example.shop_mall_back.user.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ReviewTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("리뷰 목록 테스트")
    public void findAll(){
        List<Review> reviewList = new ArrayList<>();
        reviewList = reviewRepository.findAll();

        reviewList.forEach(review -> System.out.println(review));
    }

    @Test
    @DisplayName("리뷰 삽입 테스트")
    public void inset(){
        Long memberId = 1L;
        Long orderId = 1L;
        Long productId = 1L;

        Review review = new Review();

        review.setMemberId(memberId);
        review.setOrderId(orderId);
        review.setProductId(productId);

        review.setReviewSummation("테스트");
        review.setReviewContent("테스트 내용");
        review.setReviewScore(1);
        review.setCreatedAt(LocalDateTime.now());
        review.setReviewStatus(ReviewStatus.Normal);
        reviewRepository.save(review);

        System.out.println(review.toString());

    }
}
