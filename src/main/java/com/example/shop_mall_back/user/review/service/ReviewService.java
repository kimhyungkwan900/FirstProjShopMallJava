package com.example.shop_mall_back.user.review.service;

import com.example.shop_mall_back.user.review.domain.Review;
import com.example.shop_mall_back.user.review.dto.ReviewDTO;
import com.example.shop_mall_back.user.review.dto.ReviewFormDTO;
import com.example.shop_mall_back.user.review.dto.ReviewListDTO;
import com.example.shop_mall_back.user.review.dto.ReviewUpdateDTO;
import com.example.shop_mall_back.user.review.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    // 특정 리뷰
    public ReviewDTO findByReviewId(Long id) {
        return reviewRepository.findById(id).stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("찾는 리뷰가 없습니다"));
    }

    // 상품 별로 리뷰 목록
    public ReviewListDTO findAllByProductId(Long productId) {
        List<Review> reviews = reviewRepository.findAllByProductId(productId);

        List<ReviewDTO> reviewDTOList = reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .toList();

        double average = reviews.stream()
                .mapToInt(Review::getReviewScore)
                .average()
                .orElse(0.0);

        return ReviewListDTO.builder()
                .reviewList(reviewDTOList)
                .averageScore(Math.round(average * 10) / 10.0)
                .build();
    }

    // 회원 별로 리뷰 목록
    public List<ReviewDTO> findAllByMemberId(Long memberId) {
        List<Review> reviews = reviewRepository.findAllByMemberId(memberId);
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
    public void updateReview(Long id, ReviewUpdateDTO reviewUpdateDTO) {
        Review review = reviewRepository.findById(id)
                .orElseThrow( () -> new InvalidParameterException("리뷰를 찾을 수 없습니다."));
        review.setReviewScore(reviewUpdateDTO.getScore());
        review.setReviewContent(reviewUpdateDTO.getContent());
        review.setReviewSummation(reviewUpdateDTO.getSummation());
        review.setUpdatedAt(reviewUpdateDTO.getUpdatedAt());
        reviewRepository.save(review);
    }

    public Review findEntityById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 리뷰를 찾을 수 없습니다. id=" + id));
    }

}