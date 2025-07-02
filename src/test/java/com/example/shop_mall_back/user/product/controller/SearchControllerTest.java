package com.example.shop_mall_back.user.product.controller;

import com.example.shop_mall_back.user.product.dto.SearchKeywordDto;
import com.example.shop_mall_back.user.product.service.SearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @Test
    @DisplayName("POST /api/search - 검색 키워드 기록")
    void recordSearch_ShouldReturnSavedKeyword() throws Exception {
        // given
        SearchKeywordDto savedKeyword = SearchKeywordDto.builder()
                .id(1L)
                .keyword("노트북")
                .count(3) // 예시
                .build();

        given(searchService.recordSearch("노트북")).willReturn(savedKeyword);

        // when & then
        mockMvc.perform(post("/api/search")
                        .param("keyword", "노트북")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)) // @RequestParam용 설정
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.keyword").value("노트북"))
                .andExpect(jsonPath("$.count").value(3));
    }
    }