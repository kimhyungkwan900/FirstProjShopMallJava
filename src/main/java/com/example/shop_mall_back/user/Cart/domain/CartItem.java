package com.example.shop_mall_back.user.Cart.domain;

import com.example.shop_mall_back.common.domain.Cart;
import com.example.shop_mall_back.common.domain.Product;
import jakarta.persistence.*;
import jdk.jshell.Snippet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@NoArgsConstructor

public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "is_selected", nullable = false)
    private Boolean isSelected = false;  // 주문을 위한 선택 여부

    @Column(name = "is_sold_out",  nullable = false)
    private Boolean isSoldOut = false;  // 품절 여부

}
