package com.example.shop_mall_back.user.reviewTest;

import com.example.shop_mall_back.user.review.domain.enums.ReviewStatus;
import com.example.shop_mall_back.user.review.dto.ReviewDTO;
import com.example.shop_mall_back.user.review.dto.ReviewFormDTO;
import com.example.shop_mall_back.user.review.dto.ReviewListDTO;
import com.example.shop_mall_back.user.review.dto.ReviewUpdateDTO;
import com.example.shop_mall_back.user.review.service.ReviewService;
import org.hibernate.query.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class reiewServiceTests {
    @Autowired
    private ReviewService reviewService;

    @Test
    @DisplayName("리뷰 등록 테스트")
    public void insertReviewTest(){
        ReviewFormDTO reviewFormDTO = new ReviewFormDTO();
        reviewFormDTO.setScore(4);
        reviewFormDTO.setMemberId(2L);
        reviewFormDTO.setOrderId(2L);
        reviewFormDTO.setProductId(1L);
        reviewFormDTO.setSummation("좋은거같아요");
        reviewFormDTO.setReviewContent("아주 좋은거 같아요");
        reviewFormDTO.setCreatedAt(LocalDateTime.now());
        reviewFormDTO.setReviewStatus(ReviewStatus.normal);

        reviewService.insertReview(reviewFormDTO);
    }

    @Test
    @DisplayName("리뷰 삭제 테스트")
    public void deleteReviewTest(){
        Long id = 2L;
        reviewService.deleteReview(id);
    }

    @Test
    @DisplayName("리뷰 수정 테스트")
    public void updateReviewTest() {
        // DTO 객체 생성 및 값 설정
        Long id = 1L;
        String summation = "수정 되었습니다.";
        int score = 1;
        String content = "수정되었습니다.";
        ReviewUpdateDTO reviewUpdateDTO = new ReviewUpdateDTO();
        reviewUpdateDTO.setScore(score);
        reviewUpdateDTO.setReviewContent(content);
        reviewUpdateDTO.setSummation(summation);
        reviewUpdateDTO.setUpdatedAt(LocalDateTime.now());
        // 리뷰 상태 설정 (예: Normal 또는 Blinded)
        // 수정 메서드 호출
//        reviewService.updateReview(id,  reviewUpdateDTO);
    }

    @Test
    @DisplayName("상품별 리뷰 목록")
    public void findReviewByProductIdTest(){
        Long productId = 1L;
        String sort = "createdAt";
        ReviewListDTO list = reviewService.findAllByProductId(productId, sort);
        list.getReviewList().forEach(System.out::println);
    }

//    @Test
//    @DisplayName("회원별 리뷰 목록")
//    public void findReviewByMemberIdTest(){
//        Long memberId = 2L;
//        Page<ReviewDTO> list = reviewService.findAllByMemberId(memberId);
//        list.forEach(System.out::println);
//    }
}

