package com.example.shop_mall_back.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileUpdateDTO {
    private Long memberId;
    private String nickname;

    private String profileImgUrl;
}