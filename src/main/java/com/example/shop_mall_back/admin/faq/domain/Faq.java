package com.example.shop_mall_back.admin.faq.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name ="faq")
@Getter
@Setter
@NoArgsConstructor
public class Faq {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //자동으로 숫자 up
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;

    private LocalDateTime created_at; //등록일
    private LocalDateTime updated_at; //수정일

    @PrePersist //데이터가 저장되기 전에 실행
    protected void onCreate(){
        this.created_at = LocalDateTime.now();
    }
    @PreUpdate //데이터가 수정되기 전에 실행
    protected void onUpdate() {
        this.updated_at = LocalDateTime.now();
    }

}
