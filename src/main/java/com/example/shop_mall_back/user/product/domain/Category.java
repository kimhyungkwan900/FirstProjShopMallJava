package com.example.shop_mall_back.user.product.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "categories")
@Builder
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL 등에서 사용하는 auto-increment 전략
    private Long id; // 카테고리 고유 ID

    @Column(nullable = false)
    private String name; // 카테고리 이름

    @ManyToOne(fetch = FetchType.LAZY) // 부모 카테고리를 참조하는 다대일 관계 설정, 지연 로딩 방식
    @JoinColumn(name = "parent_id") // 외래 키(FK) 이름을 parent_id로 설정
    private Category parent; // 자기 자신을 참조하는 부모 카테고리

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL) // 자식 카테고리 리스트. 부모 엔티티가 삭제되면 자식도 같이 삭제됨
    private List<Category> children = new ArrayList<>(); // 자식 카테고리들 (1:N 관계)
}

