package com.example.shop_mall_back.reviewTest;

import com.example.shop_mall_back.user.review.dto.ReviewListDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ControllerTest {

    @Autowired
    private ReviewController reviewController;

    @Test
    public void reviewListTest() {
        Long productId = 1L;
        ReviewListDTO reviewListDTO = reviewController.findAllByProductId(productId);

        reviewListDTO.getReviewList().forEach(System.out::println);
    }

}
