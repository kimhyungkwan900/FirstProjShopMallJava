package com.example.shop_mall_back.user.review.controller;

import com.example.shop_mall_back.user.review.dto.*;
import com.example.shop_mall_back.user.review.service.ReviewImgService;
import com.example.shop_mall_back.user.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ReviewListDTO findAllByProductId(
            @RequestParam("productId") Long productId,
            @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort) {
        return reviewService.findAllByProductId(productId, sort);
    }

    // 회원별 리뷰 목록 컨트롤
    @GetMapping("/mypage/reviews")
    public Page<ReviewDTO> findAllByMemberId(
            @RequestParam("memberId") Long memberId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        return reviewService.findAllByMemberId(memberId, page, size);
    }

    // 리뷰 받아오기
    @GetMapping("/mypage/review/update")
    public ReviewDTO findById(@RequestParam("reviewId") Long reviewId) {
        ReviewDTO dto = reviewService.findByReviewId(reviewId);
        return dto;
    }

    // 리뷰 등록
    @PostMapping(value = "/mypage/review/writer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void insertReview(
            @RequestPart("reviewFormDTO") ReviewFormDTO reviewFormDTO,
            @RequestPart(value = "reviewImgFile", required = false) List<MultipartFile> reviewImgFile) {

        reviewService.insertReview(reviewFormDTO, reviewImgFile);
    }

    // 리뷰 수정
    @PutMapping(value = "/mypage/review/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateReview(
            @RequestParam("reviewId") Long id,
            @RequestPart("review") ReviewUpdateDTO reviewUpdateDTO,
            @RequestPart(value = "reviewImgFile", required = false) List<MultipartFile> reviewImgFile,
            @RequestParam(value = "keepImageIds", required = false) List<Long> keepImageIds // ✅ 이거 꼭 추가
    ) {
        reviewService.updateReview(id, reviewUpdateDTO, reviewImgFile, keepImageIds);

    }

    // 리뷰 삭제
    @DeleteMapping("/mypage/review/delete")
    public void deleteReview(@RequestParam("reviewId") Long id){
        reviewService.deleteReview(id);
    }

    //리뷰 별점만 받아오기
    @GetMapping("/starRating")
    public double getStarRating(@RequestParam("productId") Long productId) {
        return reviewService.StarRating(productId);
    }


}
