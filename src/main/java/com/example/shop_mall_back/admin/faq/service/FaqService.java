package com.example.shop_mall_back.admin.faq.service;

import com.example.shop_mall_back.admin.faq.domain.Faq;
import com.example.shop_mall_back.admin.faq.dto.FaqDto;
import com.example.shop_mall_back.admin.faq.dto.FaqSearchDto;
import com.example.shop_mall_back.admin.faq.repository.FaqRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class FaqService {
    private final FaqRepository faqRepository;


    //1. FAQ 등록
    public Long createFaq(FaqDto faqDto) {
        Faq faq = faqDto.toEntity();
        faqRepository.save(faq);
        return faq.getId();
    }

    // 2. 전체 목록 페이징
    @Transactional(readOnly = true)
    public Page<Faq> getFaqList(Pageable pageable) {
        return faqRepository.findAll(pageable);
    }

    //3. 검색(카테고리는 필수로, 키워드는 옵션임)
    @Transactional(readOnly = true)
    public Page<Faq> searchFaqs(FaqSearchDto faqSearchDto, Pageable pageable){
        return faqRepository.searchFaqs(faqSearchDto, pageable);
    }

    // 4. 상세 조회
    @Transactional(readOnly = true)
    public Faq getFaqById(Long id) {
        return faqRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FAQ not found. ID: " + id));
    }

    // 5. 수정
    public void updateFaq(Long id, FaqDto faqDto) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FAQ not found. ID: " + id));

        faq.setCategory(faqDto.getCategory());
        faq.setQuestion(faqDto.getQuestion());
        faq.setAnswer(faqDto.getAnswer());
    }

    // 6. 삭제
    public void deleteFaq(Long id) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FAQ not found. ID: " + id));

        faqRepository.delete(faq);
    }

}
