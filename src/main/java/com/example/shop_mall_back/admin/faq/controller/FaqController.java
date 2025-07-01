package com.example.shop_mall_back.admin.faq.controller;

import com.example.shop_mall_back.admin.faq.dto.FaqDto;
import com.example.shop_mall_back.admin.faq.repository.FaqRepository;
import com.example.shop_mall_back.admin.faq.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/admin/faqs")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    //등록
    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody FaqDto faqDto) {
        return ResponseEntity.ok(faqService.createFaq(faqDto));
    }

    //목록 전체 조회
    @GetMapping("/list")
    public ResponseEntity<List<FaqDto>> list() {
        return ResponseEntity.ok(faqService.getAllFaqs());
    }

    //하나만 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<FaqDto> detail(@PathVariable Long id) {
        return ResponseEntity.ok(faqService.getFaq(id));
    }

    //수정
    @PutMapping("/modify/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody FaqDto faqDto) {
        faqService.updateFaq(id, faqDto);
        return ResponseEntity.ok().build();
    }

    //삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        faqService.deleteFaq(id);
        return ResponseEntity.noContent().build();
    }
}
