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

//    Page<OrderReturn> findByMemberId(Long memberId, Pageable pageable);
//
//    Page<OrderReturn> findByMemberIdAndReturnType(Long memberId, OrderReturn.ReturnType returnType, Pageable pageable);

    @Query("""
    select o from OrderReturn o 
    where o.memberId = :memberId
    and (:returnTypes is null or o.returnType in :returnTypes)
    and not exists(
        select 1 from OrderDelete od where od.orderId = o.orderId
    )
    order by o.regDate desc 
""")
    Page<OrderReturn> findNonDeleteReturnsByMemberId(
            @Param("memberId") Long memberId,
            @Param("returnTypes") List<OrderReturn.ReturnType> returnTypes,
            Pageable pageable);
}
