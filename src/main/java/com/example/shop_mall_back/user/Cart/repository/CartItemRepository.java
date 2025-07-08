package com.example.shop_mall_back.user.Cart.repository;

import com.example.shop_mall_back.common.domain.Cart;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.Cart.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * 특정 장바구니(cart) 안에서 동일한 상품(product)과 옵션(selectedOption)을 가진 항목이 존재하는지 조회
     * → 상품 추가 시, 기존 항목과 중복되는지 확인하는 데 사용됨
     */
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    /**
     * 해당 장바구니(cart)에 담긴 모든 장바구니 항목들을 조회
     * → 사용자의 장바구니 페이지를 불러올 때 사용
     */
    List<CartItem> findByCart(Cart cart);

    /**
     * 특정 회원(memberId)의 장바구니에 담긴 모든 항목을 삭제
     * → 장바구니 전체 비우기 기능에 사용
     */
    void deleteByCart_Member_Id(Long memberId);

    /**
     * 특정 회원(memberId)의 장바구니 중 선택된 항목만 삭제
     * → 사용자가 체크박스로 선택한 항목만 삭제하고 싶을 때 사용
     */
    void deleteByCart_Member_IdAndIsSelectedTrue(Long memberId);

    /**
     * 선택된 장바구니 항목들의 총 금액을 계산
     * → 장바구니에서 선택된 상품들의 총 결제 예상 금액을 보여줄 때 사용
     * JPQL을 사용하여 product의 가격 * 수량을 모두 더한 값을 반환
     * 품절된 상품은 계산에서 제외
     */

    @Query("select sum(ci.product.price * ci.quantity) " +
            "from CartItem ci " +
            "where ci.cart.member.id = :memberId and ci.isSelected = true and ci.isSoldOut = false")
    Integer calculateSelectedTotalAmount(@Param("memberId") Long memberId);

    List<CartItem> findByCartMemberId(Long memberId);

    List<CartItem> findByCart_Member_Id(Long id);

}
