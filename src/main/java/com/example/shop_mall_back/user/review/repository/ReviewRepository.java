package com.example.shop_mall_back.user.review.repository;

import com.example.shop_mall_back.user.review.domain.Review;
import com.example.shop_mall_back.user.review.dto.ReviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {


    // 회원 별 리뷰 목록
    List<Review> findAllByMemberId(@Param("memberId") Long memberId);
    // 상품 별 리뷰 목록
    List<Review> findAllByProductId(@Param("productId") Long productId);


}
