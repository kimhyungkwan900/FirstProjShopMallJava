package com.example.shop_mall_back.user.review.controller;

import com.example.shop_mall_back.user.review.dto.*;
import com.example.shop_mall_back.user.review.service.ReviewImgService;
import com.example.shop_mall_back.user.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    // 상품별 리뷰 목록 컨트롤
    @GetMapping("/product/review")
    public ReviewListDTO findAllByProductId(@RequestParam("productId") Long productId) {
        return reviewService.findAllByProductId(productId);
    }
    // 회원별 리뷰 목록 컨트롤
    @GetMapping("/mypage/reviews")
    public List<ReviewDTO> findAllByMemberId(@RequestParam("memberId") Long memberId) {
        return reviewService.findAllByMemberId(memberId);
    }

    // 리뷰 받아오기
    @GetMapping("/mypage/review/update")
    public ReviewDTO findById(@RequestParam("reviewId") Long reviewId) {
        ReviewDTO dto = reviewService.findByReviewId(reviewId);
        return dto;
    }

    // 리뷰 등록 이미지 등록
    @PostMapping("/mypage/review/writer")
    public void insertReview(@RequestBody ReviewFormDTO reviewFormDTO) {
        reviewService.insertReview(reviewFormDTO);
    }

    // 리뷰 수정 이미지 수정
    @PutMapping(value = "/mypage/review/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateReview(
            @RequestParam("reviewId") Long id,
            @RequestPart("review") ReviewUpdateDTO reviewUpdateDTO,
            @RequestPart(value = "reviewImgFile", required = false) List<MultipartFile> reviewImgFile
    ) {
        reviewService.updateReview(id, reviewUpdateDTO, reviewImgFile);
    }

    // 리뷰 삭제
    @DeleteMapping("/mypage/review/delete")
    public void deleteReview(@RequestParam("reviewId") Long id){
        reviewService.deleteReview(id);
    }





}
