package com.example.shop_mall_back.admin.faq.service;

import com.example.shop_mall_back.admin.faq.domain.Faq;
import com.example.shop_mall_back.admin.faq.dto.FaqDto;
import com.example.shop_mall_back.admin.faq.dto.FaqSearchDto;
import com.example.shop_mall_back.admin.faq.dto.PageRequestDto;
import com.example.shop_mall_back.admin.faq.dto.PageResponseDto;
import com.example.shop_mall_back.admin.faq.repository.FaqRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FaqService {

    private final FaqRepository faqRepository;

    // 전체 목록 조회 + 페이징
    @Transactional(readOnly = true)
    public PageResponseDto<FaqDto> findAll(PageRequestDto requestDto) {
        Pageable pageable = requestDto.toPageable();
        Page<FaqDto> result = faqRepository.findAll(pageable)
                .map(FaqDto::new);

        return PageResponseDto.<FaqDto>withAll()
                .dtoList(result.getContent())
                .pageRequestDto(requestDto)
                .totalCount(result.getTotalElements())
                .build();
    }

    // 검색 + 페이징
    @Transactional(readOnly = true)
    public PageResponseDto<FaqDto> searchFaqs(FaqSearchDto faqSearchDto, PageRequestDto requestDto) {
        Pageable pageable = requestDto.toPageable();
        Page<FaqDto> result = faqRepository.searchFaqs(faqSearchDto, (PageRequestDto) pageable)
                .map(FaqDto::new);

        return PageResponseDto.<FaqDto>withAll()
                .dtoList(result.getContent())
                .pageRequestDto(requestDto)
                .totalCount(result.getTotalElements())
                .build();
    }

    // 등록
    public Long createFaq(FaqDto faqDto) {
        Faq faq = faqDto.toEntity();
        faqRepository.save(faq);
        return faq.getId();
    }

    // 상세 조회
    @Transactional(readOnly = true)
    public FaqDto getFaqById(Long id) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 id의 FAQ를 찾을 수 없습니다"));

        return new FaqDto(faq);
    }

    // 수정
    public void updateFaq(Long id, FaqDto faqDto) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 id의 FAQ를 찾을 수 없습니다"));

        faq.setCategory(faqDto.getCategory());
        faq.setQuestion(faqDto.getQuestion());
        faq.setAnswer(faqDto.getAnswer());
    }

    // 삭제 (일괄삭제 가능)
    public void deleteFaqs(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("삭제할 항목이 없습니다");
        }

        for (Long id : ids) {
            Faq faq = faqRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("해당하는 id의 FAQ를 찾을 수 없습니다"));
            faqRepository.delete(faq);
        }
    }
}
