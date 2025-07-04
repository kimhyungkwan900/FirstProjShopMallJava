package com.example.shop_mall_back.admin.faq.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//검색 조건용 DTO
public class FaqSearchDto {
    private String category; //카테고리 선택(필수)
    private String keyWord; //검색어(옵션)
}
