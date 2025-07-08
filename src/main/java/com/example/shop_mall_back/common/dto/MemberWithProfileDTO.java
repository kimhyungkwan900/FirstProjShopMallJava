package com.example.shop_mall_back.common.dto;

import com.example.shop_mall_back.common.constant.Age;
import com.example.shop_mall_back.common.constant.Gender;
import com.example.shop_mall_back.common.constant.Grade;
import com.example.shop_mall_back.common.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberWithProfileDTO {
    private Long id;
    private String email;
    private String userId;
    private String phoneNumber;

    // 프로필 정보
    private String nickname;
    private Role role;
    private Grade grade;
    private Gender gender;
    private Age age;
    private String profileImgUrl;
    private String delivAddress;
}