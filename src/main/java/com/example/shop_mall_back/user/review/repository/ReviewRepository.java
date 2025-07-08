package com.example.shop_mall_back.user.review.repository;

import com.example.shop_mall_back.user.review.domain.Review;
import com.example.shop_mall_back.user.review.dto.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {

    List<Review> findAllByMemberId(@Param("memberId") Long memberId);

    List<Review> findAllByProductId(@Param("productId") Long productId);

    Page<Review> findByMemberId(Long memberId, Pageable pageable);

    boolean existsByOrderId(Long orderId);

}
