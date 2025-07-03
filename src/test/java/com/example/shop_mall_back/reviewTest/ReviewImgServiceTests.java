package com.example.shop_mall_back.reviewTest;

import com.example.shop_mall_back.user.review.domain.Review;
import com.example.shop_mall_back.user.review.domain.ReviewImg;
import com.example.shop_mall_back.user.review.domain.enums.ImageType;
import com.example.shop_mall_back.user.review.dto.ReviewImgDTO;
import com.example.shop_mall_back.user.review.repository.ReviewImgRepository;
import com.example.shop_mall_back.user.review.service.ReviewFileService;
import com.example.shop_mall_back.user.review.service.ReviewImgService;
import com.example.shop_mall_back.user.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
public class ReviewImgServiceTests {

    @InjectMocks
    private ReviewImgService reviewImgService;

    @Mock
    private ReviewFileService reviewFileService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private ReviewImgRepository reviewImgRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("리뷰 이미지 등록 테스트")
    public void testSaveReviewImage() throws Exception {
        // given
        byte[] content = "dummy image content".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", content);

        Long reviewId = 1L;

        // Mockito Stubbing
        when(reviewFileService.saveFile(any(MultipartFile.class))).thenReturn("/images/test.jpg");

        Review mockReview = new Review(); // 실제 Review 객체 생성
        // 필요한 필드가 있으면 세팅
        when(reviewService.findEntityById(reviewId)).thenReturn(mockReview);

        ReviewImg mockReviewImg = new ReviewImg();
        mockReviewImg.setReview(mockReview);
        mockReviewImg.setFilePath("/test.jpg");
        mockReviewImg.setFileSize(content.length);
        mockReviewImg.setImageType(ImageType.JPG);

        when(reviewImgRepository.save(any(ReviewImg.class))).thenReturn(mockReviewImg);

        // when
        ReviewImgDTO result = reviewImgService.saveReviewImage(reviewId, multipartFile);

        // then
        System.out.println("저장된 이미지 경로: " + result.getFilePath());
    }
}