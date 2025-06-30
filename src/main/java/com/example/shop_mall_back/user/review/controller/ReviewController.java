package com.example.shop_mall_back.user.review.controller;

import com.example.shop_mall_back.user.review.dto.ReviewDTO;
import com.example.shop_mall_back.user.review.dto.ReviewFormDTO;
import com.example.shop_mall_back.user.review.dto.ReviewListDTO;
import com.example.shop_mall_back.user.review.dto.ReviewUpdateDTO;
import com.example.shop_mall_back.user.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 상품별 리뷰 목록 컨트롤
    @GetMapping("/products")
    public ReviewListDTO findAllByProductId(@RequestParam("productId") Long productId) {
        return reviewService.findAllByProductId(productId);
    }
    // 회원별 리뷰 목록 컨트롤
    @GetMapping("/mypage/reviewlist")
    public List<ReviewDTO> findAllByMemberId(@RequestParam("memberId") Long memberId) {
        return reviewService.findAllByMemberId(memberId);
    }
    // 리뷰 등록
    @PostMapping("/mypage/review/writer")
    public void insertReview(@RequestBody ReviewFormDTO reviewFormDTO) {
        reviewService.insertReview(reviewFormDTO);
    }
    // 리뷰 수정
    @PutMapping("/mypage/review/update")
    public void updateReview(@RequestParam("reviewId") Long id, @RequestBody ReviewUpdateDTO reviewUpdateDTO) {
        reviewService.updateReview(id, reviewUpdateDTO);
    }


}
