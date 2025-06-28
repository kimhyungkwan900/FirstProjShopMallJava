package com.example.shop_mall_back.user.product.dto;

import com.example.shop_mall_back.user.product.domain.SearchKeyword;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchKeywordDto {
    // 검색 키워드 고유 ID (Primary Key)
    private Long id;

    // 검색 키워드 문자열 (예: "청바지", "노트북")
    private String keyword;

    // 해당 키워드가 검색된 횟수 (인기 검색어 기준)
    private int count;

    public static SearchKeywordDto from(SearchKeyword keyword) {
        return SearchKeywordDto.builder()
                .id(keyword.getId())           // ID 복사
                .keyword(keyword.getKeyword()) // 키워드 문자열 복사
                .count(keyword.getCount())     // 검색 횟수 복사
                .build();                      // DTO 객체 생성
    }
}
