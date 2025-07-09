package com.example.shop_mall_back.user.product.domain;

import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // JPA에서 이 클래스가 데이터베이스 테이블과 매핑되는 엔티티임을 선언
@Getter // 모든 필드에 대한 getter 메서드를 Lombok이 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성 (JPA에서 필수로 요구됨)
@Table(name = "wishlist_items") // 이 엔티티가 매핑될 테이블 이름을 "wishlist_items"로 지정
@IdClass(WishlistItemId.class) // 복합 키(Composite Key)를 정의한 클래스 지정
@Builder // 빌더 패턴을 사용하여 객체 생성 가능
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 자동 생성
public class WishlistItem {

    @Id // 복합 키의 일부로 지정
    @ManyToOne // 회원과의 다대일 관계 (여러 WishlistItem → 한 Member)
    @JoinColumn(name = "member_id") // 외래 키 컬럼명 지정 (회원 ID)
    private Member user; // 찜한 사용자의 정보

    @Id // 복합 키의 일부로 지정
    @ManyToOne // 상품과의 다대일 관계 (여러 WishlistItem → 한 Product)
    @JoinColumn(name = "product_id") // 외래 키 컬럼명 지정 (상품 ID)
    private Product product; // 찜한 상품의 정보

    // 명시적으로 사용자 설정 가능하도록 set 메서드 정의
    public void setUser(Member member) {
        this.user = member;
    }

    // 명시적으로 상품 설정 가능하도록 set 메서드 정의
    public void setProduct(Product product) {
        this.product = product;
    }
}

