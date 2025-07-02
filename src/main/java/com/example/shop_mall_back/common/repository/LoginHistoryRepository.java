package com.example.shop_mall_back.common.repository;

import com.example.shop_mall_back.common.domain.login.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

}
