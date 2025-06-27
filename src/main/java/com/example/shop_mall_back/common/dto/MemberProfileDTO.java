package com.example.shop_mall_back.common.dto;

import com.example.shop_mall_back.common.constant.Age;
import com.example.shop_mall_back.common.constant.Gender;
import com.example.shop_mall_back.common.constant.Grade;
import com.example.shop_mall_back.common.constant.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class MemberProfileDTO {
    private Long id;
    private Long memberId;
    private Role role;
    private String name;
    private Grade grade;
    private boolean isMemberShip;
    private String nickname;
    private String profile_img_url;
    private Gender gender;
    private Age age;
    private String delivAddress;
}
