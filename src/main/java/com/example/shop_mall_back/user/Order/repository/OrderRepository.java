package com.example.shop_mall_back.user.Order.repository;

import com.example.shop_mall_back.common.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByMemberAddressId(Long deliveryAddressId);
}
