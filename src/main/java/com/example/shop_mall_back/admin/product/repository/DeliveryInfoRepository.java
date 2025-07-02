package com.example.shop_mall_back.admin.product.repository;

import com.example.shop_mall_back.admin.product.domain.DeliveryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo, Long> {
}
