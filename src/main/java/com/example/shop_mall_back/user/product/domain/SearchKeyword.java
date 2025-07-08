package com.example.shop_mall_back.user.product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // JPA 엔티티임을 나타냄. 해당 클래스는 DB 테이블과 매핑됨
@Getter // 모든 필드에 대한 getter 메서드를 Lombok이 자동 생성
@Setter // 모든 필드에 대한 setter 메서드를 Lombok이 자동 생성
@Table(name = "search_keywords") // 이 엔티티가 매핑될 테이블 이름을 "search_keywords"로 지정
@NoArgsConstructor // 파라미터가 없는 기본 생성자 자동 생성 (JPA 필수 요건)
public class SearchKeyword {

    @Id // 기본 키(primary key) 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 전략 사용 (MySQL 등에서 PK 자동 증가)
    private Long id; // 고유 ID 값

    @Column(nullable = false, unique = true) // NOT NULL + UNIQUE 제약 조건
    private String keyword; // 사용자 검색어 (중복 저장 방지)

    @Column(nullable = false) // NOT NULL 제약 조건
    private int count = 0; // 검색 횟수 (기본값 0으로 설정)
}

