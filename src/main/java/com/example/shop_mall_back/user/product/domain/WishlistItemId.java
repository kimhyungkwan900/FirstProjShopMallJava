package com.example.shop_mall_back.user.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WishlistItemId implements Serializable {

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "product_id")
    private Long productId;
}
