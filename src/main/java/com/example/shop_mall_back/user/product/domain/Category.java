package com.example.shop_mall_back.user.product.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity // JPA에서 이 클래스가 테이블과 매핑된 엔티티임을 명시
@Getter // 모든 필드에 대한 getter 메서드 자동 생성 (Lombok)
@NoArgsConstructor // 파라미터 없는 기본 생성자 생성 (JPA가 내부적으로 사용함)
@Table(name = "categories") // 이 엔티티가 매핑될 테이블 이름을 "categories"로 지정
@Builder // 빌더 패턴을 사용하여 객체 생성 가능
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자 자동 생성
public class Category {

    @Id // 기본 키(primary key) 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL 등에서 사용하는 auto-increment 전략
    private Long id; // 카테고리 고유 ID

    @Column(nullable = false) // name 필드는 NOT NULL 제약 조건
    private String name; // 카테고리 이름

    @ManyToOne(fetch = FetchType.LAZY) // 부모 카테고리를 참조하는 다대일 관계 설정, 지연 로딩 방식
    @JoinColumn(name = "parent_id") // 외래 키(FK) 이름을 parent_id로 설정
    private Category parent; // 자기 자신을 참조하는 부모 카테고리

    // @Builder.Default // (선택사항) 빌더 패턴 사용할 때 기본값을 명시적으로 설정할 수 있음
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL) // 자식 카테고리 리스트. 부모 엔티티가 삭제되면 자식도 같이 삭제됨
    private List<Category> children = new ArrayList<>(); // 자식 카테고리들 (1:N 관계)
}

