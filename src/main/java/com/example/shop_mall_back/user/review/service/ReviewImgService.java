package com.example.shop_mall_back.user.review.service;

import com.example.shop_mall_back.user.review.repository.ReviewImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewImgService {

    @Value("${reviewImgLocation}")
    private String reviewImgLocation;


    private final ReviewImgRepository reviewImgRepository;
}
