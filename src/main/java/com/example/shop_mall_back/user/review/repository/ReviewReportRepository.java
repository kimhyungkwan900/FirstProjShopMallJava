package com.example.shop_mall_back.user.review.repository;

import com.example.shop_mall_back.admin.review.dto.AdminReviewReportDTO;
import com.example.shop_mall_back.user.review.domain.ReviewReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewReportRepository extends JpaRepository<ReviewReport, Long> {
    int countByReviewId(Long reviewId);

    List<ReviewReport> findAllByReviewId(Long reviewId);

}
