package com.example.shop_mall_back.admin.order.repository;

import com.example.shop_mall_back.admin.order.domain.OrderManage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderManageRepository extends JpaRepository<OrderManage, Long>, OrderManageRepositoryCustom {
}
