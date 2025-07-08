package com.example.shop_mall_back.user.reviewTest;

import com.example.shop_mall_back.user.review.domain.Review;
import com.example.shop_mall_back.user.review.domain.enums.ReviewStatus;
import com.example.shop_mall_back.user.review.dto.ReviewDTO;
import com.example.shop_mall_back.user.review.dto.ReviewFormDTO;
import com.example.shop_mall_back.user.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ReviewTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("ReviewDTO -> Review 엔티티 매핑 테스트")
    public void insertReview() {
        ReviewFormDTO reviewFormDTO = new ReviewFormDTO();
        reviewFormDTO.setScore(3);
        reviewFormDTO.setSummation("좋아요");
        reviewFormDTO.setReviewContent("아주아주 좋아요");
        reviewFormDTO.setMemberId(4L);
        reviewFormDTO.setProductId(1L);
        reviewFormDTO.setOrderId(6L);
        reviewFormDTO.setReviewStatus(ReviewStatus.normal);
        reviewFormDTO.setCreatedAt(LocalDateTime.now());

        Review review = reviewFormDTO.createReview();
        reviewRepository.save(review);
    }

    @Test
    @DisplayName("회원 id별 리뷰 확인")
    public void findByMemberId(){
        Long memberId = 2L;
        List<Review> reviews = reviewRepository.findAllByMemberId(memberId);
        reviews.forEach(System.out::println);
    }

    @Test
    @DisplayName("상품 별 리뷰 확인")
    public void findByProductId(){
        Long productId = 1L;
        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        reviews.forEach(System.out::println);
    }



}
