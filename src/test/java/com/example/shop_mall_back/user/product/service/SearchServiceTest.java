package com.example.shop_mall_back.user.product.service;

import com.example.shop_mall_back.user.product.domain.SearchKeyword;
import com.example.shop_mall_back.user.product.dto.SearchKeywordDto;
import com.example.shop_mall_back.user.product.repository.SearchKeywordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SearchServiceTest {

    @Mock
    private SearchKeywordRepository searchKeywordRepository;

    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void recordSearch_ExistingKeyword_ShouldIncreaseCount() {
        // given
        SearchKeyword existing = new SearchKeyword();
        existing.setKeyword("shoes");
        existing.setCount(3);

        when(searchKeywordRepository.findByKeyword("shoes"))
                .thenReturn(Optional.of(existing));
        when(searchKeywordRepository.save(existing))
                .thenReturn(existing);

        // when
        SearchKeywordDto result = searchService.recordSearch("shoes");

        // then
        assertThat(result.getKeyword()).isEqualTo("shoes");
        assertThat(result.getCount()).isEqualTo(4); // 기존 count + 1
        verify(searchKeywordRepository).findByKeyword("shoes");
        verify(searchKeywordRepository).save(existing);
    }

    @Test
    void recordSearch_NewKeyword_ShouldCreateNewEntry() {
        // given
        when(searchKeywordRepository.findByKeyword("bags"))
                .thenReturn(Optional.empty());

        SearchKeyword saved = new SearchKeyword();
        saved.setKeyword("bags");
        saved.setCount(1);

        when(searchKeywordRepository.save(any(SearchKeyword.class)))
                .thenReturn(saved);

        // when
        SearchKeywordDto result = searchService.recordSearch("bags");

        // then
        assertThat(result.getKeyword()).isEqualTo("bags");
        assertThat(result.getCount()).isEqualTo(1);
        verify(searchKeywordRepository).findByKeyword("bags");
        verify(searchKeywordRepository).save(any(SearchKeyword.class));
    }
}
