package com.example.shop_mall_back.user.review.dto;

import com.example.shop_mall_back.user.review.domain.ReviewImg;
import com.example.shop_mall_back.user.review.domain.enums.ImageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewImgDTO {
    private Long id;

    private Long reviewId;

    private String filePath;

    private ImageType imageType;

    private int fileSize;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ReviewImgDTO of(ReviewImg reviewImg) {
        if (reviewImg == null) return null;
        return modelMapper.map(reviewImg, ReviewImgDTO.class);
    }
}
