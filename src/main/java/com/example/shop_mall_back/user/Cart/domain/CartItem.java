package com.example.shop_mall_back.user.Cart.domain;

import com.example.shop_mall_back.common.domain.Cart;
import com.example.shop_mall_back.common.domain.Product;
import jakarta.persistence.*;
import jdk.jshell.Snippet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_tem")
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

    @Column(nullable = false)
    private String selectedOption;

    @Column(name = "is_selected")
    private Boolean isSelected;  // 주문을 위한 선택 여부

    @Column(name = "is_sold_out")
    private Boolean isSoldOut;  // 품절 여부

}
