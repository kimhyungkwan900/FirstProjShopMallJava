package com.example.shop_mall_back.user.Order.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PaymentDto {
    private String paymentMethod;
    private String paymentToken;
    private int amount;
}
