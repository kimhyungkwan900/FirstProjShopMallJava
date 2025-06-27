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

    @PostMapping
    public SearchKeywordDto recordSearch(@RequestParam String keyword) {
        return searchService.recordSearch(keyword);
    }
}