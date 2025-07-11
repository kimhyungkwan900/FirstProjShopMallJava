package com.example.shop_mall_back.user.review.service;

import com.example.shop_mall_back.admin.review.dto.AdminReviewReportDTO;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberProfile;
import com.example.shop_mall_back.common.dto.MemberProfileDTO;
import com.example.shop_mall_back.common.repository.MemberProfileRepository;
import com.example.shop_mall_back.common.repository.MemberRepository;
import com.example.shop_mall_back.common.service.serviceimpl.MemberProfileServiceImpl;
import com.example.shop_mall_back.user.product.repository.ProductRepository;
import com.example.shop_mall_back.user.product.service.ProductService;
import com.example.shop_mall_back.user.review.domain.Review;
import com.example.shop_mall_back.user.review.domain.ReviewImg;
import com.example.shop_mall_back.user.review.dto.*;
import com.example.shop_mall_back.user.review.repository.ReviewImgRepository;
import com.example.shop_mall_back.user.review.repository.ReviewReactionRepository;
import com.example.shop_mall_back.user.review.repository.ReviewReportRepository;
import com.example.shop_mall_back.user.review.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final ReviewReactionService reviewReactionService;
    private final ReviewImgService reviewImgService;
    private final ProductRepository  productRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final ReviewImgRepository reviewImgRepository;
    private final ReviewFileService reviewFileService;


    // 리뷰 받아오기(수정)
    public ReviewDTO findByReviewId(Long id) {
        ReviewDTO dto = reviewRepository.findById(id).stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("찾는 리뷰가 없습니다"));
        dto.setReviewImgDTOList(reviewImgService.getImagesByReviewId(id));
        return dto;
    }
    // 상품 별로 리뷰 목록
    public ReviewListDTO findAllByProductId(Long productId, String sort) {
        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        // 정렬 조건에 따라 분기
        if ("like".equalsIgnoreCase(sort)) {
            // 좋아요 수 기준 내림차순 정렬
            reviews = reviews.stream()
                    .sorted((r1, r2) -> {
                        int like1 = reviewReactionService.findLikeCountByReviewId(r1.getId());
                        int like2 = reviewReactionService.findLikeCountByReviewId(r2.getId());
                        return Integer.compare(like2, like1); // 내림차순
                    })
                    .toList();
        } else {
            // 최신순 정렬 (id 또는 createdAt 기준)
            reviews = reviews.stream()
                    .sorted(Comparator.comparing(Review::getCreatedAt).reversed()) // 또는 Review::getId
                    .toList();
        }
        // DTO 변환 및 추가 정보 세팅
        List<ReviewDTO> reviewDTOList = reviews.stream()
                .map(review -> {
                    ReviewDTO dto = modelMapper.map(review, ReviewDTO.class);
                    dto.setLikeCount(reviewReactionService.findLikeCountByReviewId(review.getId()));
                    dto.setDislikeCount(reviewReactionService.findDislikeCountByReviewId(review.getId()));
                    dto.setReviewImgDTOList(reviewImgService.getImagesByReviewId(review.getId()));
                    MemberProfile memberProfile = memberProfileRepository.findByMemberId(review.getMember().getId());
                    dto.setMemberNickname(memberProfile.getNickname());
                    return dto;
                }).toList();

        // 평균 평점 계산
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
//    public List<ReviewDTO> findAllByMemberId(Long memberId) {
//        List<Review> reviews = reviewRepository.findAllByMemberId(memberId).stream()
//                .sorted(Comparator.comparing(Review::getId))
//                .toList();
//        // 반환할 때 DTO로 변환하면서 like count dislike count 숫자를 추가해서 dto에 담아 return
//        return reviews.stream()
//                .map(review -> {
//                    ReviewDTO dto = modelMapper.map(review, ReviewDTO.class);
//                    dto.setLikeCount(reviewReactionService.findLikeCountByReviewId(review.getId()));
//                    dto.setDislikeCount(reviewReactionService.findDislikeCountByReviewId(review.getId()));
//                    dto.setReviewImgDTOList(reviewImgService.getImagesByReviewId(review.getId()));
//                    return dto;
//                }).toList();
//    }
    // 리뷰 등록
    public void insertReview(ReviewFormDTO reviewFormDTO, List<MultipartFile> reviewImgFile) {

        reviewFormDTO.setCreatedAt(LocalDateTime.now());

        Review review = modelMapper.map(reviewFormDTO, Review.class);
        reviewRepository.save(review);

        if(reviewImgFile != null && !reviewImgFile.isEmpty() ) {
            for(MultipartFile file : reviewImgFile) {
                reviewImgService.saveReviewImage(review.getId(), file);
            }
        }
    }

    // 리뷰 삭제
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    // 리뷰 수정
    public void updateReview(Long id, ReviewUpdateDTO reviewUpdateDTO,
                             List<MultipartFile> reviewImgFile,
                             List<Long> keepImageIds) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new InvalidParameterException("리뷰를 찾을 수 없습니다."));

        review.setReviewScore(reviewUpdateDTO.getScore());
        review.setReviewContent(reviewUpdateDTO.getReviewContent());
        review.setReviewSummation(reviewUpdateDTO.getSummation());
        review.setUpdatedAt(LocalDateTime.now());

        reviewRepository.save(review);

        // 모든 기존 이미지 가져오기
        List<ReviewImg> reviewImages = reviewImgRepository.findByReviewId(review.getId());

        for (ReviewImg img : reviewImages) {
            // keepImageIds에 없는 이미지는 삭제
            if (keepImageIds == null || !keepImageIds.contains(img.getId())) {
                try {
                    reviewFileService.deleteFile(img.getFilePath());
                    reviewImgRepository.deleteById(img.getId());
                } catch (Exception e) {
                    System.out.println("삭제 실패: " + e.getMessage());
                }
            }
        }

        // 새 이미지 파일 저장
        if (reviewImgFile != null && !reviewImgFile.isEmpty()) {
            for (MultipartFile file : reviewImgFile) {
                reviewImgService.saveReviewImage(id, file);
            }
        }
    }



    public Review findEntityById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 리뷰를 찾을 수 없습니다. id=" + id));
    }

    // 회원 별로 리뷰 목록
    public Page<ReviewDTO> findAllByMemberId(Long memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Review> reviewPage = reviewRepository.findByMemberId(memberId, pageable);

        // 개별 리뷰들을 DTO로 변환하면서 추가 정보 포함
        return reviewPage.map(review -> {
            ReviewDTO dto = modelMapper.map(review, ReviewDTO.class);
            dto.setLikeCount(reviewReactionService.findLikeCountByReviewId(review.getId()));
            dto.setDislikeCount(reviewReactionService.findDislikeCountByReviewId(review.getId()));
            // 상품 이름 받아오기
            Product product = productRepository.findById(review.getProduct().getId()).get();
            dto.setProductName(product.getName());
            dto.setReviewImgDTOList(reviewImgService.getImagesByReviewId(review.getId()));
            return dto;
        });
    }

    // 별점 받아오기
    public double StarRating(Long productId) {
        Double average = reviewRepository.findAverageScoreByProductId(productId);
        if (average == null) {
            return 0.0;
        }
        return Math.round(average * 10) / 10.0; // 소수점 첫째자리 반올림
    }

}