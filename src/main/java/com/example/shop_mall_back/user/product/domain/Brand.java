package com.example.shop_mall_back.user.product.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // JPA에서 이 클래스가 테이블과 매핑된다는 것을 의미
@Getter // 모든 필드에 대해 getter 메서드를 자동 생성 (Lombok)
@NoArgsConstructor // 기본 생성자 자동 생성 (JPA에서 반드시 필요)
@Table(name = "brands") // 이 엔티티가 매핑될 테이블 이름을 "brands"로 지정
@Builder // 빌더 패턴을 통해 객체 생성 가능
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 자동 생성
public class Brand {

    @Id // 기본 키(PK) 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 증가 설정 (MySQL의 auto_increment에 해당)
    private Long id; // 브랜드 ID (PK)

    @Column(nullable = false, unique = true) // NOT NULL, 유일(unique) 제약 조건 설정
    private String name; // 브랜드 이름
}
