package com.example.shop_mall_back.user.product.repository;

import com.example.shop_mall_back.user.product.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    /*
      브랜드 이름으로 존재 여부를 확인하는 메서드
      메서드 이름만으로 JPA가 자동으로 쿼리를 생성 (파라미터 name이 일치하는 브랜드가 존재하는지 검사)
      예: SELECT COUNT(*) > 0 FROM brand WHERE name = ?
     */
    boolean existsByName(String name);
}
