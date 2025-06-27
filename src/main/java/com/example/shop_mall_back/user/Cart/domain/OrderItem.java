package com.example.shop_mall_back.user.Cart.domain;

import com.example.shop_mall_back.common.domain.Order;
import com.example.shop_mall_back.common.domain.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    // 주문 항목 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주문 (부모 테이블: orders)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_orderitem_order_id"))
    private Order order;

    // 주문된 상품 (products 테이블 참조)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_orderitem_product_id"))
    private Product product;

    // 주문 수량
    @Column(nullable = false)
    private int quantity;

    // 주문 당시 가격
    @Column(nullable = false)
    private int price;

    // 선택 옵션 정보 (예: 색상, 사이즈 등)
    @Column(name = "selected_option")
    private String selectedOption;



}
