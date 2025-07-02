package com.example.shop_mall_back.admin.faq.service;

import com.example.shop_mall_back.admin.faq.domain.Faq;
import com.example.shop_mall_back.admin.faq.dto.FaqDto;
import com.example.shop_mall_back.admin.faq.repository.FaqRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FaqService {
    private final FaqRepository faqRepository;

    //등록
    public Long createFaq(FaqDto faqDto){
        Faq faq = faqDto.toEntity(); // DTO를 Entity로 변환
        faqRepository.save(faq); //DB에 저장
        return faq.getId(); //저장된 ID반환
    }

    //전체 조회
    public List<FaqDto> getAllFaqs(){
        return faqRepository.findAllByOrderByCreatedAtDesc()//db에서 faq를 최순으로 불러와서 정렬
                .stream()
                .map(FaqDto::new)//각각의 FAQ를 FaqDto로 바꿔줌
                .collect(Collectors.toList());
    }

    //하나만 조회
    public FaqDto getFaq(Long id){
        Faq faq = faqRepository.findById(id) //id로 DB에서 해당 FAQ를 찾아
                .orElseThrow(()-> new IllegalArgumentException("FAQ 없음")); //없으면 예외발생
        return new FaqDto(faq);
    }

    // 수정
    public void updateFaq(Long id, FaqDto dto) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FAQ 없음"));
        faq.setQuestion(dto.getQuestion());
        faq.setAnswer(dto.getAnswer());
    }

    // 삭제
    public void deleteFaq(Long id) {

        faqRepository.deleteById(id);
    }

}
