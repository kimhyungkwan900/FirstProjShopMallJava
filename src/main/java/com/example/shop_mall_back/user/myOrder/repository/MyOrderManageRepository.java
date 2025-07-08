package com.example.shop_mall_back.user.myOrder.repository;

import com.example.shop_mall_back.admin.order.domain.OrderManage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MyOrderManageRepository extends JpaRepository<OrderManage, Long> {

     @Query("SELECT o.orderStatus FROM OrderManage o WHERE o.id = :orderId")
     OrderManage.OrderStatus findOrderStatusByOrderId(@Param("orderId") Long orderId);
}
