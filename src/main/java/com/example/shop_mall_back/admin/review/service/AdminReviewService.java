package com.example.shop_mall_back.admin.review.service;

import com.example.shop_mall_back.admin.review.domain.ReviewBlind;
import com.example.shop_mall_back.admin.review.dto.AdminReviewBlindDTO;
import com.example.shop_mall_back.admin.review.dto.AdminReviewDTO;
import com.example.shop_mall_back.admin.review.repository.AdminReviewRepository;
import com.example.shop_mall_back.user.review.domain.Review;
import com.example.shop_mall_back.user.review.domain.enums.ReviewStatus;
import com.example.shop_mall_back.user.review.dto.ReviewDTO;
import com.example.shop_mall_back.user.review.repository.ReviewRepository;
import com.example.shop_mall_back.user.review.service.ReviewImgService;
import com.example.shop_mall_back.user.review.service.ReviewReactionService;
import com.example.shop_mall_back.user.review.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminReviewService {
    private final AdminReviewRepository adminReviewRepository;
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final ReviewReactionService reviewReactionService;
    private final ReviewImgService reviewImgService;

    //리뷰 블라인드 처리
    @Transactional
    public void reviewBlind(AdminReviewBlindDTO reviewBlindDTO){
        ReviewDTO reviewDTO = reviewService.findByReviewId(reviewBlindDTO.getReviewId());
        reviewDTO.setReviewStatus(ReviewStatus.blinded);
        // 리뷰 status 상태 업데이트
        Review review =  modelMapper.map(reviewDTO, Review.class);
        reviewRepository.save(review);
        // 리뷰 블라인드 사유 입력
        ReviewBlind reviewBlind = modelMapper.map(reviewBlindDTO,ReviewBlind.class);
        adminReviewRepository.save(reviewBlind);
    }

    //리뷰 블라인드 해제 - 블라인드 내역 삭제
    @Transactional
    public void reviewUnblind(Long reviewId, Long blindId){
        ReviewDTO reviewDTO = reviewService.findByReviewId(reviewId);
        // 리뷰의 status 상태를 normal로 바꾸고 저장
        reviewDTO.setReviewStatus(ReviewStatus.normal);
        Review review =  modelMapper.map(reviewDTO, Review.class);
        reviewRepository.save(review);
        // 블라인드 처리한 정보 삭제
        adminReviewRepository.deleteById(blindId);
    }


    public Page<ReviewDTO> findByReviewAll(Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAll(pageable);

        return reviews.map(review -> {
            ReviewDTO dto = modelMapper.map(review, ReviewDTO.class);
            dto.setLikeCount(reviewReactionService.findLikeCountByReviewId(review.getId()));
            dto.setDislikeCount(reviewReactionService.findDislikeCountByReviewId(review.getId()));
            dto.setReviewImgDTOList(reviewImgService.getImagesByReviewId(review.getId()));
            return dto;
        });
    }
}
