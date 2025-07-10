package com.example.shop_mall_back.user.myOrder.repository;

import com.example.shop_mall_back.common.domain.Order;
import com.example.shop_mall_back.user.myOrder.domain.OrderReturn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository

public interface MyOrderRepository extends JpaRepository<Order, Integer> {

    Page<Order> findByMemberId(Long memberId, Pageable pageable);

    @Query(value = """
    SELECT DISTINCT o.*
    FROM orders o
    JOIN order_item oi ON o.id = oi.order_id
    JOIN products p ON oi.product_id = p.id
    WHERE o.member_id = :memberId
      AND (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%'))
      AND (:startDate IS NULL OR o.order_date >= :startDate)
      AND (:endDate IS NULL OR o.order_date <= :endDate)
    ORDER BY o.order_date DESC
    """,
            countQuery = """
    SELECT COUNT(DISTINCT o.id)
    FROM orders o
    JOIN order_item oi ON o.id = oi.order_id
    JOIN products p ON oi.product_id = p.id
    WHERE o.member_id = :memberId
      AND (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%'))
      AND (:startDate IS NULL OR o.order_date >= :startDate)
      AND (:endDate IS NULL OR o.order_date <= :endDate)
    """,
            nativeQuery = true)
    Page<Order> findOrdersByFilterNative(
            @Param("memberId") Long memberId,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    Order findById(Long orderId);
}
