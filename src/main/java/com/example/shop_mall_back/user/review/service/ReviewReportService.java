package com.example.shop_mall_back.user.review.service;

import com.example.shop_mall_back.admin.review.dto.AdminReviewReportDTO;
import com.example.shop_mall_back.common.repository.MemberRepository;
import com.example.shop_mall_back.user.review.domain.ReviewReport;
import com.example.shop_mall_back.user.review.dto.ReviewReportFormDTO;
import com.example.shop_mall_back.user.review.repository.ReviewReportRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewReportService {
    private final ReviewReportRepository reviewReportRepository;
    private final MemberRepository memberRepository;

    private final ModelMapper modelMapper;
    // 신고 등록
    public void insertReviewReport(ReviewReportFormDTO reviewReportFormDTO){
        reviewReportFormDTO.setCreatedAt(LocalDateTime.now());
        ReviewReport reviewReport = modelMapper.map(reviewReportFormDTO, ReviewReport.class);
        reviewReportRepository.save(reviewReport);
    }

    // 신고 개수 가져오기
    public int countReviewReportByReviewId(Long reviewId) {
        return reviewReportRepository.countByReviewId(reviewId);
    }

    // 신고 리스트 가져오기
    public List<AdminReviewReportDTO> findReviewReportByReviewId(Long reviewId) {
        List<ReviewReport> reviewReportList = reviewReportRepository.findAllByReviewId(reviewId);

        return reviewReportList.stream().map(reviewReport -> {
            AdminReviewReportDTO dto = modelMapper.map(reviewReport, AdminReviewReportDTO.class);

            memberRepository.findById(reviewReport.getMemberId())
                    .ifPresent(member -> dto.setUserId(member.getUserId()));

            return dto;
        }).collect(Collectors.toList());
    }

    //
}
