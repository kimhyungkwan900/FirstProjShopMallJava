package com.example.shop_mall_back.admin.order.repository;

import com.example.shop_mall_back.admin.order.domain.ClaimManage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimManageRepository extends JpaRepository<ClaimManage,Long>, ClaimManageRepositoryCustom {
}
