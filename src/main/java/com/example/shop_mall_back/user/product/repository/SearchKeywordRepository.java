package com.example.shop_mall_back.user.product.repository;

import com.example.shop_mall_back.user.product.domain.SearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {
    /*
      키워드 문자열을 기준으로 검색 기록을 조회
      - 검색 횟수를 누적하기 위해 기존에 입력된 키워드가 있는지 확인할 때 사용
      - 예: SELECT * FROM search_keyword WHERE keyword = ?
      - Optional을 사용하여 결과가 없을 수도 있음을 처리
     */
    Optional<SearchKeyword> findByKeyword(String keyword);
}
