package com.example.shop_mall_back.user.Order.repository;

import com.example.shop_mall_back.user.Order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
