package com.example.shop_mall_back.user.myOrder.repository;

import com.example.shop_mall_back.common.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface MyOrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findByMemberId(Long memberId, Pageable pageable);
}
