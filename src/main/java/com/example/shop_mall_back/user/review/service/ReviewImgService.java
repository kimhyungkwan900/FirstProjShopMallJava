package com.example.shop_mall_back.user.review.service;

import com.example.shop_mall_back.user.review.domain.Review;
import com.example.shop_mall_back.user.review.domain.ReviewImg;
import com.example.shop_mall_back.user.review.domain.enums.ImageType;
import com.example.shop_mall_back.user.review.dto.ReviewImgDTO;
import com.example.shop_mall_back.user.review.repository.ReviewImgRepository;
import com.example.shop_mall_back.user.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReviewImgService {

    // 이미지 파일 저장 경로 (application.properties에서 주입받음)
    @Value("${reviewImgLocation}")
    private String reviewImgLocation;

    private final ReviewImgRepository reviewImgRepository; // DB 저장용 레포지토리
    private final ReviewFileService reviewFileService;                 // 실제 파일을 디스크에 저장/삭제하는 서비스
    private final ReviewRepository reviewRepository;             // 리뷰 엔티티 조회용 서비스

    /**
     * 리뷰 이미지 저장
     * - 파일을 서버에 저장
     * - Review 엔티티와 연관된 ReviewImg 엔티티 생성
     * - DB에 저장 후 DTO 반환
     */
    public ReviewImgDTO saveReviewImage(Long reviewId, MultipartFile file) {
        // 파일 저장 후 경로 반환
        String savedPath = reviewFileService.saveFile(file);

        // 리뷰 엔티티 조회 (연관관계 설정을 위해)
        Review review = reviewRepository.findById(reviewId).orElseThrow();

        // ReviewImg 엔티티 생성 및 값 설정
        ReviewImg reviewImg = new ReviewImg();
        reviewImg.setReview(review); // 연관된 리뷰 설정
        reviewImg.setFilePath(savedPath); // 저장된 경로
        reviewImg.setFileSize((int) file.getSize()); // 파일 크기
        reviewImg.setImageType(getImageType(file.getContentType())); // MIME 타입으로 이미지 타입 결정

        // DB에 저장하고 DTO로 변환 후 반환
        ReviewImg saved = reviewImgRepository.save(reviewImg);
        return ReviewImgDTO.of(saved);
    }

    /**
     * 특정 리뷰 ID에 해당하는 이미지 목록 조회
     */
    public List<ReviewImgDTO> getImagesByReviewId(Long reviewId) {
        return reviewImgRepository.findByReviewId(reviewId)
                .stream()
                .map(ReviewImgDTO::of) // 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }
    /**
     * 리뷰 이미지 삭제
     * - 실제 파일도 삭제
     * - DB에서도 삭제
     */
    public void deleteReviewImage(Long reviewId) {
        List<ReviewImg> reviewImgs = reviewImgRepository.findByReviewId(reviewId);

        for (ReviewImg reviewImg : reviewImgs) {
            try {
                System.out.println(reviewImg.getFilePath().toString());
                reviewFileService.deleteFile(reviewImg.getFilePath());
                reviewImgRepository.deleteById(reviewImg.getId());
            } catch (Exception e) {
                log.error("리뷰 이미지 삭제 실패: {}", reviewImg.getFilePath(), e);
            }
        }
    }

    /**
     * MIME 타입에 따라 ImageType enum 매핑
     */
    private ImageType getImageType(String contentType) {
        if (contentType.contains("jpeg") || contentType.contains("jpg")) return ImageType.JPG;
        else if (contentType.contains("png")) return ImageType.PNG;
        else if (contentType.contains("gif")) return ImageType.GIF;
        else throw new IllegalArgumentException("지원하지 않는 이미지 형식입니다.");
    }
}