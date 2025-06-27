package com.example.shop_mall_back.user.review.service;

import com.example.shop_mall_back.user.review.domain.Review;
import com.example.shop_mall_back.user.review.dto.ReviewDTO;
import com.example.shop_mall_back.user.review.dto.ReviewFormDTO;
import com.example.shop_mall_back.user.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    // 상품 별로 리뷰 목록
    public List<ReviewDTO> findAllByProductId(Long productId) {
        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .toList();
    }

    // 회원 별로 리뷰 목록
    public List<ReviewDTO> findAllByMemberId(Long memderId) {
        List<Review> reviews = reviewRepository.findAllByMemberId(memderId);
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .toList();
    }
    // 리뷰 등록
    public void insertReview(ReviewFormDTO reviewFormDTO) {
        Review review = modelMapper.map(reviewFormDTO, Review.class);
        reviewRepository.save(review);
    }

    // 리뷰 삭제
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    // 리뷰 수정
    public void updateReview(Long id, int score,  String content, String summation) {
        Review review = reviewRepository.findById(id).get();
        review.setReviewScore(score);
        review.setReviewContent(content);
        review.setReviewSummation(summation);
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }
}