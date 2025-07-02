package com.example.shop_mall_back.user.Cart.repository;

import com.example.shop_mall_back.common.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 특정 회원 ID(memberId)에 해당하는 장바구니(Cart)를 조회하는 메서드

 * - 사용 시점 예시:
 *   사용자가 장바구니에 상품을 담거나 조회할 때, 해당 사용자의 장바구니가 이미 존재하는지 확인
 *   존재하지 않는 경우 새로 생성하는 로직과 연계

 * - 반환 타입: Optional<Cart>
 *   장바구니가 존재하지 않을 경우 null 대신 Optional.empty()를 반환하여 NullPointerException을 방지
 *
 * - Spring Data JPA의 쿼리 메서드 명명 규칙에 따라 자동 구현
 *   → "findByMember_Id"는 Cart 엔티티의 member 필드의 id 값을 조건으로 검색함을 의미
 */
public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByMember_Id(Long memberId);
}