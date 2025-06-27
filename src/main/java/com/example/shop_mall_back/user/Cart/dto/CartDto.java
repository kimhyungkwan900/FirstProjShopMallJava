package com.example.shop_mall_back.user.Cart.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CartDto {

    private Long id;    //장바구니 id
    private Long member_id; //사용자 id (Member 테이블 참조)
    private LocalDateTime created_at; //장바구니 생성 시간
}
