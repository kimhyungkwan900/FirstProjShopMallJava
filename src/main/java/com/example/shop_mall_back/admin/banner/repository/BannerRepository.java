package com.example.shop_mall_back.admin.banner.repository;

import com.example.shop_mall_back.admin.banner.domain.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    @Query("SELECT b FROM Banner b WHERE b.isActive = true AND b.visibleFrom <= CURRENT_TIMESTAMP AND b.visibleTo >= CURRENT_TIMESTAMP ORDER BY b.displayOrder ASC")
    List<Banner> findVisibleBanners();
}
