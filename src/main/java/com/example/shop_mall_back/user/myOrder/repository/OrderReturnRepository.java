package com.example.shop_mall_back.user.myOrder.repository;

import com.example.shop_mall_back.user.myOrder.domain.OrderReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderReturnRepository extends JpaRepository<OrderReturn, Integer> {

    @Query("SELECT o.returnType FROM OrderReturn o WHERE o.orderId = :orderId")
    OrderReturn.ReturnType getReturnTypeByOrderId(@Param("orderId") Long orderId);
}
