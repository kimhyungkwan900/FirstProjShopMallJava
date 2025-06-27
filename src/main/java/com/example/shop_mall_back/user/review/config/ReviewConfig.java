package com.example.shop_mall_back.user.review.config;

import com.example.shop_mall_back.user.review.domain.Review;
import com.example.shop_mall_back.user.review.dto.ReviewDTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReviewConfig {
    @Bean
    public ModelMapper modelMapper() {
        // ModelMapper 인스턴스를 생성
        ModelMapper modelMapper = new ModelMapper();

        // Review 엔티티 → ReviewDTO 변환 시,
        // 엔티티의 getReviewScore() 값을 DTO의 setScore()로 매핑되도록 수동 설정
        modelMapper.typeMap(Review.class, ReviewDTO.class)
                .addMapping(Review::getReviewScore, ReviewDTO::setScore);
        // 매핑 설정이 완료된 ModelMapper 빈을 반환하여
        // 스프링 컨테이너에서 주입 받을 수 있게 함
        return modelMapper;
    }
}
