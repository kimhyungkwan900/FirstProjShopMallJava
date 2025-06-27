package com.example.shop_mall_back.user.review.repository;

import com.example.shop_mall_back.user.review.domain.Review;
import com.example.shop_mall_back.user.review.dto.ReviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

}
