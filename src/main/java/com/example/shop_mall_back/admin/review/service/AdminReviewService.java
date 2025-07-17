package com.example.shop_mall_back.admin.review.service;

import com.example.shop_mall_back.admin.review.domain.ReviewBlind;
import com.example.shop_mall_back.admin.review.dto.AdminReviewBlindDTO;
import com.example.shop_mall_back.admin.review.dto.AdminReviewDTO;
import com.example.shop_mall_back.admin.review.repository.AdminReviewRepository;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.repository.MemberRepository;
import com.example.shop_mall_back.user.product.repository.ProductRepository;
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
    private final ProductRepository  productRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void reviewBlind(AdminReviewBlindDTO reviewBlindDTO){
        Review review = reviewRepository.findById(reviewBlindDTO.getReviewId())
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        review.setReviewStatus(ReviewStatus.blinded);

        // 리뷰 블라인드 사유 등록
        reviewBlindDTO.setBlindAt(LocalDateTime.now());
        ReviewBlind reviewBlind = modelMapper.map(reviewBlindDTO, ReviewBlind.class);
        adminReviewRepository.save(reviewBlind);
    }

    @Transactional
    public void reviewUnblind(Long reviewId){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        review.setReviewStatus(ReviewStatus.normal);
        // 엔티티 상태 변경만 하면 JPA가 자동으로 반영함

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
            if ("writer".equalsIgnoreCase(searchType)) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("member").get("userId"), "%" + keyword + "%"));
            } else if ("product".equalsIgnoreCase(searchType)) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("product").get("name"), "%" + keyword + "%"));
            }
        }
        Page<Review> reviews = reviewRepository.findAll(spec, pageable);
        return reviews.map(review -> {
            AdminReviewDTO dto = modelMapper.map(review, AdminReviewDTO.class);
            dto.setReviewImgDTOList(reviewImgService.getImagesByReviewId(review.getId()));
            // 상품 명 받아와서 값 넣음
            Product product = productRepository.findById(review.getProduct().getId()).get();
            dto.setProductName(product.getName());
            // 회원 id 받아오기
            Member member = memberRepository.findById(review.getMember().getId()).get();
            dto.setUserId(member.getUserId());
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
