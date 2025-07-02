package com.example.shop_mall_back.user.Cart.repository;

import com.example.shop_mall_back.user.Cart.domain.DeliveryFeeRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryFeeRuleRepository extends JpaRepository<DeliveryFeeRule, Long> {
    //데이터가 없을 수도 있기 때문에 null이 반환될 가능성
    //optional로 처리해 null 값이 반환될 가능성이 있는 상황에서 명시적으로 null 처리를 강제하기 위해 사용
    Optional<Object> findTopByOrderByIdDesc();
}
