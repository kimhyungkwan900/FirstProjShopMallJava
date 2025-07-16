package com.example.shop_mall_back.admin.order.repository;

import com.example.shop_mall_back.admin.order.domain.ClaimManage;
import com.example.shop_mall_back.admin.order.dto.ClaimSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClaimManageRepositoryCustom {
    Page<ClaimManage> getClaimPageByCondition(ClaimSearchDto claimSearchDto, Pageable pageable);
}
