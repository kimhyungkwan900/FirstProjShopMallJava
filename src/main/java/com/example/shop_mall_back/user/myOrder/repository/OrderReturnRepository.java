package com.example.shop_mall_back.user.myOrder.repository;

import com.example.shop_mall_back.user.myOrder.domain.OrderReturn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderReturnRepository extends JpaRepository<OrderReturn, Integer> {

    @Query("SELECT o.returnType FROM OrderReturn o WHERE o.orderId = :orderId")
    OrderReturn.ReturnType getReturnTypeByOrderId(@Param("orderId") Long orderId);

    // 전체 목록
    Page<OrderReturn> findByMemberId(Long memberId, Pageable pageable);

    // 필터 조건 포함 페이징
    Page<OrderReturn> findByMemberIdAndReturnType(Long memberId, OrderReturn.ReturnType returnType, Pageable pageable);

}
