package com.example.shop_mall_back.user.product.controller;

import com.example.shop_mall_back.user.product.dto.SearchKeywordDto;
import com.example.shop_mall_back.user.product.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    // POST 방식으로 검색 키워드를 서버에 전송하여 기록
    @PostMapping
    public SearchKeywordDto recordSearch(@RequestParam String keyword) {
        return searchService.recordSearch(keyword);// 클라이언트에서 전달한 키워드를 서비스에 전달하고, 저장된 검색 키워드 정보를 응답으로 반환
    }
}