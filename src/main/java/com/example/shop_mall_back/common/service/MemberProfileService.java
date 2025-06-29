package com.example.shop_mall_back.common.service;

import com.example.shop_mall_back.common.domain.Member;
import com.example.shop_mall_back.common.domain.MemberProfile;
import com.example.shop_mall_back.common.dto.MemberProfileDTO;

public interface MemberProfileService {

    MemberProfileDTO getMemberProfile(Long memberId);

    void memberProfileUpdate(MemberProfileDTO memberProfileDTO);

    default MemberProfileDTO entityToDTO(MemberProfile memberProfile) {
        return MemberProfileDTO.builder()
                .id(memberProfile.getId())
                .memberId(memberProfile.getMember().getId())
                .role(memberProfile.getRole())
                .grade(memberProfile.getGrade())
                .name(memberProfile.getName())
                .isMembership(memberProfile.isMembership())
                .nickname(memberProfile.getNickname())
                .profileImgUrl(memberProfile.getProfileImgUrl())
                .gender(memberProfile.getGender())
                .age(memberProfile.getAge())
                .delivAddress(memberProfile.getDelivAddress())
                .build();
    }

    default MemberProfile dtoToEntity(MemberProfileDTO memberProfileDTO, Member member) {
        return MemberProfile.builder()
                .id(memberProfileDTO.getId())
                .member(member) //JPA는 외래키를 실제 Member 객체로 매핑을 하기 때문에 해당 객체를 조회해서 변경하는 구조
                .role(memberProfileDTO.getRole())
                .grade(memberProfileDTO.getGrade())
                .name(memberProfileDTO.getName())
                .isMembership(memberProfileDTO.isMembership())
                .nickname(memberProfileDTO.getNickname())
                .profileImgUrl(memberProfileDTO.getProfileImgUrl())
                .gender(memberProfileDTO.getGender())
                .age(memberProfileDTO.getAge())
                .delivAddress(memberProfileDTO.getDelivAddress())
                .build();
    }
}
