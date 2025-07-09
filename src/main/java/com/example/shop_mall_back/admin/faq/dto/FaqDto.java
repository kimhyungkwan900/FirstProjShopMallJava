package com.example.shop_mall_back.admin.faq.dto;

import com.example.shop_mall_back.admin.faq.domain.Faq;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class FaqDto {

    private Long id; // FAQ 번호
    private String category; //카테고리 분류(배송,환불 등)
    private String question; // 질문
    private String answer; // 답변
    private LocalDateTime createdAt;


    // 엔티티를 받아서 DTO로 바꾸는 생성자
    public FaqDto(Faq faq) {
        this.id = faq.getId();
        this.category = faq.getCategory();
        this.question = faq.getQuestion();
        this.answer = faq.getAnswer();
        this.createdAt = faq.getCreatedAt();
    }

    // DTO → Entity로 바꾸는 메서드
    public Faq toEntity() {
        Faq faq = new Faq();
        faq.setCategory(this.category);
        faq.setQuestion(this.question);
        faq.setAnswer(this.answer);
        return faq;
    }

}