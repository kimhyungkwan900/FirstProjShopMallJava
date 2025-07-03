package com.example.shop_mall_back.admin.review.controller;

import com.example.shop_mall_back.admin.review.dto.AdminReviewBlindDTO;
import com.example.shop_mall_back.admin.review.service.AdminReviewService;
import com.example.shop_mall_back.user.review.dto.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private AdminReviewService adminReviewService;
    //관리자 리뷰 목록 받아오기
    @GetMapping
    public Page<ReviewDTO> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return adminReviewService.findByReviewAll(pageable);
    }

    // 리뷰 블라인드 처리
    @PostMapping
    public void reviewBlind(@RequestBody AdminReviewBlindDTO adminReviewDTO){
        adminReviewService.reviewBlind(adminReviewDTO);
    }
    // 리뷰 블라인드 해제(블라인드 내역도 삭제)
    @DeleteMapping
    public void reviewUnBlind(@RequestParam("reviewId") Long reviewId, @RequestParam("blindId") Long blindId){
        adminReviewService.reviewUnblind(reviewId, blindId);
    }
}
