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

    /*
      사용자가 검색한 키워드를 기록하거나, 기존에 존재하면 count를 증가시킴
      - keyword가 이미 존재할 경우 → count + 1
      - 존재하지 않으면 → 새로 생성하여 count = 1
      - 저장 후 DTO로 변환하여 반환
     */
    public SearchKeywordDto recordSearch(String keyword) {
        // 키워드로 기존 검색 기록을 조회
        Optional<SearchKeyword> found = searchKeywordRepository.findByKeyword(keyword);

        // 기존에 있으면 count 증가, 없으면 새로운 객체 생성
        SearchKeyword entity = found.map(k -> {
            k.setCount(k.getCount() + 1);// 검색 횟수 +1
            return k;
        }).orElseGet(() -> {
            SearchKeyword newKeyword = new SearchKeyword(); // 새로운 키워드 객체 생성
            newKeyword.setKeyword(keyword);
            newKeyword.setCount(1); // 처음 검색된 경우 count = 1
            return newKeyword;
        });

        // DB에 저장한 후 DTO로 변환하여 반환
        return SearchKeywordDto.from(searchKeywordRepository.save(entity));
    }
}