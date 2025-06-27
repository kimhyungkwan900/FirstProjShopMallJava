package com.example.shop_mall_back.common.dto;

import com.example.shop_mall_back.common.constant.Age;
import com.example.shop_mall_back.common.constant.Gender;
import com.example.shop_mall_back.common.constant.Grade;
import com.example.shop_mall_back.common.constant.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileDTO {
    private Long id;
    private Long memberId;

    @Builder.Default
    private Role role = Role.MEMBER;

    @Builder.Default
    private Grade grade = Grade.NORMAL;

    @Builder.Default
    private Gender gender = Gender.UNKNOWN;

    @Builder.Default
    private Age age = Age.UNKNOWN;


    @NotBlank
    @Length(min = 3, max = 20)
    private String name;

    private String nickname;
    private String delivAddress;

    @JsonProperty("profile_img_url")
    private String profileImgUrl;

    private boolean isMembership;
}
