package com.example.shop_mall_back.user.product.dto;

import com.example.shop_mall_back.user.product.domain.SearchKeyword;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchKeywordDto {
    private Long id;
    private String keyword;
    private int count;

    public static SearchKeywordDto from(SearchKeyword keyword) {
        return SearchKeywordDto.builder()
                .id(keyword.getId())
                .keyword(keyword.getKeyword())
                .count(keyword.getCount())
                .build();
    }
}
