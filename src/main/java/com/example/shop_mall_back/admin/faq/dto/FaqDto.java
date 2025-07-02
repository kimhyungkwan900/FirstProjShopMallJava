package com.example.shop_mall_back.admin.faq.dto;

import com.example.shop_mall_back.admin.faq.domain.Faq;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FaqDto {

    private Long id; // FAQ 번호
    private String question; // 질문
    private String answer; // 답변


    // 엔티티를 받아서 DTO로 바꾸는 생성자
    public FaqDto(Faq faq) {
        this.id = faq.getId();
        this.question = faq.getQuestion();
        this.answer = faq.getAnswer();
    }

    // DTO → Entity로 바꾸는 메서드
    public Faq toEntity() {
        Faq faq = new Faq();
        faq.setQuestion(this.question);
        faq.setAnswer(this.answer);
        return faq;
    }
}
