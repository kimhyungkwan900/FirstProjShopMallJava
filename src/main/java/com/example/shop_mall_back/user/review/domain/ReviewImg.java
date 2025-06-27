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
    @Column(name = "review_id", nullable = false)
    private Long reviewId;
    @Column(name = "file_path", nullable = false)
    private String filePath;
    @Column(name = "file_type", nullable = false)
    private ImageType imageType;
    @Column(name = "file_size", nullable = false)
    private int fileSize;



}
