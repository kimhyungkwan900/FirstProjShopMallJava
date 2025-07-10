package com.example.shop_mall_back.user.myOrder.repository;

import com.example.shop_mall_back.user.myOrder.domain.OrderDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyOrderDeleteRepository extends JpaRepository<OrderDelete, Long> {
    boolean existsByOrderId(Long orderId);

}
