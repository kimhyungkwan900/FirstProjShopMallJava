package com.example.shop_mall_back.user.review.domain;

import com.example.shop_mall_back.user.review.domain.enums.ImageType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "review_image")
public class ReviewImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    @Column(name = "file_size", nullable = false)
    private int fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    // 리뷰 이미지 경로 업데이트 메서드 예시
    public void updateReviewImg(String newFilePath) {
        this.filePath = newFilePath;
    }

}
