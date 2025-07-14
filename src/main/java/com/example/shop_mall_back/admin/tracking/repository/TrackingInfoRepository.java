package com.example.shop_mall_back.admin.tracking.repository;

import com.example.shop_mall_back.admin.tracking.domain.TrackingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackingInfoRepository extends JpaRepository<TrackingInfo,Long> {

    TrackingInfo findByOrderId(Long orderId);

}