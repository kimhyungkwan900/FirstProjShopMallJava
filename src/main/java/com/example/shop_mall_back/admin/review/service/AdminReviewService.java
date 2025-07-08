package com.example.shop_mall_back.admin.review.service;

import com.example.shop_mall_back.admin.review.domain.ReviewBlind;
import com.example.shop_mall_back.admin.review.dto.AdminReviewBlindDTO;
import com.example.shop_mall_back.admin.review.dto.AdminReviewDTO;
import com.example.shop_mall_back.admin.review.repository.AdminReviewRepository;
import com.example.shop_mall_back.user.review.domain.Review;
import com.example.shop_mall_back.user.review.domain.ReviewReport;
import com.example.shop_mall_back.user.review.domain.enums.ReviewStatus;
import com.example.shop_mall_back.user.review.dto.ReviewDTO;
import com.example.shop_mall_back.user.review.repository.ReviewReportRepository;
import com.example.shop_mall_back.user.review.repository.ReviewRepository;
import com.example.shop_mall_back.user.review.service.ReviewImgService;
import com.example.shop_mall_back.user.review.service.ReviewReactionService;
import com.example.shop_mall_back.user.review.service.ReviewReportService;
import com.example.shop_mall_back.user.review.service.ReviewService;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminReviewService {
    private final AdminReviewRepository adminReviewRepository;
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final ReviewImgService reviewImgService;
    private final ReviewReportService reviewReportService;

    //리뷰 블라인드 처리
    @Transactional
    public void reviewBlind(AdminReviewBlindDTO reviewBlindDTO){
        ReviewDTO reviewDTO = reviewService.findByReviewId(reviewBlindDTO.getReviewId());
        reviewDTO.setReviewStatus(ReviewStatus.blinded);
        // 리뷰 status 상태 업데이트
        Review review =  modelMapper.map(reviewDTO, Review.class);
        reviewRepository.save(review);
        // 리뷰 블라인드 사유 선택 등록
        reviewBlindDTO.setBlindAt(LocalDateTime.now());
        ReviewBlind reviewBlind = modelMapper.map(reviewBlindDTO,ReviewBlind.class);
        adminReviewRepository.save(reviewBlind);
    }

    //리뷰 블라인드 해제 - 블라인드 내역 삭제
    @Transactional
    public void reviewUnblind(Long reviewId){
        ReviewDTO reviewDTO = reviewService.findByReviewId(reviewId);
        // 리뷰의 status 상태를 normal로 바꾸고 저장
        reviewDTO.setReviewStatus(ReviewStatus.normal);
        Review review =  modelMapper.map(reviewDTO, Review.class);
        reviewRepository.save(review);
        // 블라인드 처리한 정보 삭제
        adminReviewRepository.deleteByReviewId(reviewId);
    }


    // 관리자 리뷰 필터 전체 목록 / 신고 1건 이상 있응 리뷰 목록 / 블라인드 처리한 리뷰
    public Page<AdminReviewDTO> getFilteredReviews(String filter, String searchType, String keyword, Pageable pageable) {
        Specification<Review> spec = Specification.where(null);
        if ("report".equalsIgnoreCase(filter)) {
            spec = spec.and((root, query, cb) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<ReviewReport> subRoot = subquery.from(ReviewReport.class);
                subquery.select(subRoot.get("reviewId"))
                        .where(cb.equal(subRoot.get("reviewId"), root.get("id")));
                return cb.exists(subquery);
                    });

        } else if ("blind".equalsIgnoreCase(filter)) {
            spec = spec.and((root, query, cb) -> {
                return cb.equal(root.get("reviewStatus"), ReviewStatus.blinded);
            });

        }
        if (keyword != null && !keyword.isBlank()) {
            if ("name".equalsIgnoreCase(searchType)) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("member").get("name"), "%" + keyword + "%")
                );
            } else if ("product".equalsIgnoreCase(searchType)) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("product").get("name"), "%" + keyword + "%")
                );
            }
        }
        Page<Review> reviews = reviewRepository.findAll(spec, pageable);
        return reviews.map(review -> {
            AdminReviewDTO dto = modelMapper.map(review, AdminReviewDTO.class);
            dto.setReviewImgDTOList(reviewImgService.getImagesByReviewId(review.getId()));
            dto.setReportCount(reviewReportService.countReviewReportByReviewId(review.getId())); // 추가
            // 블라인드 사유 가져와서  있으면 setter로 넣고 없으면 넣지 않는다.
            ReviewBlind reviewBlind = adminReviewRepository.findTopByReviewIdOrderByBlindAtDesc(review.getId());
            if (reviewBlind != null) {
                dto.setBlindReason(reviewBlind.getReason());
            }
            return dto;
        });
    }


}
