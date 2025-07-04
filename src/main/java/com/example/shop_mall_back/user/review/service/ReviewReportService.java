package com.example.shop_mall_back.user.review.service;

import com.example.shop_mall_back.user.review.domain.ReviewReport;
import com.example.shop_mall_back.user.review.dto.ReviewReportFormDTO;
import com.example.shop_mall_back.user.review.repository.ReviewReportRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewReportService {
    private final ReviewReportRepository reviewReportRepository;
    private final ModelMapper modelMapper;
    // 신고 등록
    public void insertReviewReport(ReviewReportFormDTO reviewReportFormDTO){
        reviewReportFormDTO.setCreatedAt(LocalDateTime.now());
        ReviewReport reviewReport = modelMapper.map(reviewReportFormDTO, ReviewReport.class);
        reviewReportRepository.save(reviewReport);
    }
}
