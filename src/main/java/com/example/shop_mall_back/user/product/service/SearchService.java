package com.example.shop_mall_back.user.product.service;

import com.example.shop_mall_back.user.product.domain.SearchKeyword;
import com.example.shop_mall_back.user.product.dto.SearchKeywordDto;
import com.example.shop_mall_back.user.product.repository.SearchKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchKeywordRepository searchKeywordRepository;

    public SearchKeywordDto recordSearch(String keyword) {
        Optional<SearchKeyword> found = searchKeywordRepository.findByKeyword(keyword);
        SearchKeyword entity = found.map(k -> {
            k.setCount(k.getCount() + 1);
            return k;
        }).orElseGet(() -> {
            SearchKeyword newKeyword = new SearchKeyword();
            newKeyword.setKeyword(keyword);
            newKeyword.setCount(1);
            return newKeyword;
        });
        return SearchKeywordDto.from(searchKeywordRepository.save(entity));
    }
}