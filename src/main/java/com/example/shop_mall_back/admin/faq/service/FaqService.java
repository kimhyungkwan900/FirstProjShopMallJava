package com.example.shop_mall_back.admin.faq.service;

import com.example.shop_mall_back.admin.faq.domain.Faq;
import com.example.shop_mall_back.admin.faq.dto.FaqDto;
import com.example.shop_mall_back.admin.faq.dto.FaqSearchDto;
import com.example.shop_mall_back.admin.faq.dto.PageRequestDto;
import com.example.shop_mall_back.admin.faq.repository.FaqRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class FaqService {

    private final FaqRepository faqRepository;


    //전체 목록 조회 + 페이징
    @Transactional(readOnly = true)
    public Page<FaqDto> findAll(Pageable pageable){
        return faqRepository.findAll(pageable)
                .map(FaqDto::new);
    }

    //검색 + 페이징
    @Transactional(readOnly = true)
    public Page<FaqDto> searchFaqs(FaqSearchDto faqSearchDto, Pageable pageable){
        return faqRepository.searchFaqs(faqSearchDto, (PageRequestDto) pageable)
                .map(FaqDto::new);
    }

    //등록
    public Long createFaq(FaqDto faqDto){
        Faq faq = new Faq();
        faqRepository.save(faq);
        return faq.getId();
    }

    //상세 조회
    @Transactional(readOnly = true)
    public FaqDto getFaqById(Long Id){
        Faq faq = faqRepository.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 id의 FAQ를 찾을 수 없습니다"));

        return new FaqDto(faq);
    }

    //수정
    public void updateFaq(Long id, FaqDto faqDto){
        Faq faq = faqRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("해당하는 id의 FAQ를 찾을 수 없습니다"));

        faq.setCategory(faqDto.getCategory());
        faq.setQuestion(faqDto.getQuestion());
        faq.setAnswer(faqDto.getAnswer());
    }

    //삭제
    public void deleteFaqs(List<Long> ids){
        if(ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("삭제할 항목이 없습니다");
        }

        for(Long id : ids) {
            Faq faq = faqRepository.findById(id)
                    .orElseThrow(()-> new EntityNotFoundException("해당하는 id의 FAQ를 찾을 수 없습니다"));
        }
    }
}
