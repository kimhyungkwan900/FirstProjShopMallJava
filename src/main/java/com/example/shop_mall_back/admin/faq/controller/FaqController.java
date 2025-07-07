package com.example.shop_mall_back.admin.faq.controller;


import com.example.shop_mall_back.admin.faq.dto.FaqDto;
import com.example.shop_mall_back.admin.faq.dto.FaqSearchDto;
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
    public Page<FaqDto> list(Pageable pageable){
        //나중에 제거
        log.info("Faq List Request - Page : {}", pageable.getPageNumber());
        return faqService.findAll(pageable);
    }

    //검색
    @GetMapping("/search")
    public Page<FaqDto> search(FaqSearchDto faqSearchDto, Pageable pageable){
        //나중에 제거
        log.info("FAQ Search: category={}, keyword={}", faqSearchDto.getCategory(), faqSearchDto.getKeyWord());
        return faqService.searchFaqs(faqSearchDto , pageable);
    }

    //등록
    @PostMapping("/create")
    public Map<String, Long> register(@RequestBody FaqDto faqDto){
        //나중에 제거
        log.info("FAQ 등록 : {}", faqDto);
        Long id = faqService.createFaq(faqDto);
        return Map.of("id", id);
    }

    //상세 조회
    @GetMapping("/{id}")
    public FaqDto get(@PathVariable Long id){
        log.info("FAQ 상세조회 : id={}", id);
        return faqService.getFaqById(id);
    }

    //수정
    @PutMapping("/update/{id}")
    public Map<String, String> modify(@PathVariable Long id, @RequestBody FaqDto faqDto){
        log.info("FAQ 수정 : id={}, data ={}", id, faqDto);
        faqService.updateFaq(id, faqDto);
        return Map.of("result", "success");
    }

    //삭제
    @DeleteMapping("/delete")
    public Map<String, String> remove(@RequestBody List<Long> ids){
        log.info("FAQ 삭제 요청: {}", ids);
        faqService.deleteFaqs(ids);
        return Map.of("result", "success");
    }

}