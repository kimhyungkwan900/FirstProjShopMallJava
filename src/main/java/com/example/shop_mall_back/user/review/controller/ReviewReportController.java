package com.example.shop_mall_back.user.review.controller;

import com.example.shop_mall_back.user.review.dto.ReviewReportFormDTO;
import com.example.shop_mall_back.user.review.service.ReviewReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewReportController {

    private final ReviewReportService reviewReportService;
    @PostMapping("/review/report")
    public void reviewReport(@RequestBody ReviewReportFormDTO ReviewReportFormDTO){
        reviewReportService.insertReviewReport(ReviewReportFormDTO);
        System.out.println("reviewId: " + ReviewReportFormDTO.getReviewId());
        System.out.println("memberId: " + ReviewReportFormDTO.getMemberId());
        System.out.println("reason: " + ReviewReportFormDTO.getReason());
        System.out.println("detail: " + ReviewReportFormDTO.getDetail());
    }

}
