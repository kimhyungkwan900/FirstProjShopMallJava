package com.example.shop_mall_back.admin.faq.controller;


import com.example.shop_mall_back.admin.faq.dto.FaqDto;
import com.example.shop_mall_back.admin.faq.dto.FaqSearchDto;
import com.example.shop_mall_back.admin.faq.dto.PageRequestDto;
import com.example.shop_mall_back.admin.faq.dto.PageResponseDto;
import com.example.shop_mall_back.admin.faq.service.FaqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/admin/faqs")
public class FaqController {

    private final FaqService faqService;

    //조회
    @GetMapping("/list")
    public PageResponseDto<FaqDto> getFaqList(@ModelAttribute PageRequestDto pageRequestDto) {
        log.info("Faq 목록 요청: page={}, size={}", pageRequestDto.getPage(), pageRequestDto.getSize());
        return faqService.findAll(pageRequestDto);
    }

    //검색
    @GetMapping("/search")
    public PageResponseDto<FaqDto> searchFaqs(
            @ModelAttribute FaqSearchDto faqSearchDto, Pageable pageable) {
        log.info("FAQ 검색 요청: category={}, keyWord={}", faqSearchDto.getCategory(), faqSearchDto.getKeyWord());
        return faqService.searchFaqs(faqSearchDto, pageable);
    }

    //등록
    @PostMapping("/create")
    public Map<String, Long> register(@RequestBody FaqDto faqDto) {
        log.info("FAQ 등록 요청: {}", faqDto);
        Long id = faqService.createFaq(faqDto);
        return Map.of("id", id);
    }

    //상세 조회
    @GetMapping("/{id}")
    public FaqDto get(@PathVariable Long id) {
        log.info("FAQ 상세 조회 요청: id={}", id);
        return faqService.getFaqById(id);
    }

    //수정
    @PutMapping("/update/{id}")
    public Map<String, String> modify(@PathVariable Long id, @RequestBody FaqDto faqDto) {
        log.info("FAQ 수정 요청: id={}, data={}", id, faqDto);
        faqService.updateFaq(id, faqDto);
        return Map.of("result", "success");
    }

    //삭제
    @DeleteMapping("/delete")
    public Map<String, String> remove(@RequestBody List<Long> ids) {
        log.info("FAQ 삭제 요청: {}", ids);
        faqService.deleteFaqs(ids);
        return Map.of("result", "success");
    }

}