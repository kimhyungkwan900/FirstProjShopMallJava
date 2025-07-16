package com.example.shop_mall_back.admin.faq.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name ="faq")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Faq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동으로 숫자 up
    private Long id;

    @Column(nullable = false, length = 50)
    private String category; // 고정값으로 사용 

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // 등록일

    @LastModifiedDate
    private LocalDateTime updatedAt; // 수정일



}