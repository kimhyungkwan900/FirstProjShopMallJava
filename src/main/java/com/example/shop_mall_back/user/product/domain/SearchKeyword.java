package com.example.shop_mall_back.user.product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "search_keywords") // 이 엔티티가 매핑될 테이블 이름을 "search_keywords"로 지정
@NoArgsConstructor
public class SearchKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 전략 사용 (MySQL 등에서 PK 자동 증가)
    private Long id; // 고유 ID 값

    @Column(nullable = false, unique = true)
    private String keyword; // 사용자 검색어 (중복 저장 방지)

    @Column(nullable = false)
    private int count = 0; // 검색 횟수 (기본값 0으로 설정)
}

