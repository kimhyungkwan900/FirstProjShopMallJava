package com.example.shop_mall_back.admin.review.dto;

import com.example.shop_mall_back.user.review.dto.ReviewDTO;
import com.example.shop_mall_back.user.review.dto.ReviewImgDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AdminReviewDTO {
    private Long id;
    private ReviewDTO reviewDTO;
    private Long AdminID;
    private String reason;
    private LocalDateTime blindAd;
}
