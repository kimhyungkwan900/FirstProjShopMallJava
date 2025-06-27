package com.example.shop_mall_back.common.service;

import com.example.shop_mall_back.common.domain.MemberProfile;
import com.example.shop_mall_back.common.dto.MemberProfileDTO;

public interface MemberProfileService {

    MemberProfileDTO getMemberProfile(Long memberId);

    void memberProfileUpdate(MemberProfileDTO memberProfileDTO);

    default MemberProfileDTO entityToDTO(MemberProfile memberProfile) {
        return MemberProfileDTO.builder()
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

    default MemberProfile dtoToEntity(MemberProfileDTO memberProfileDTO) {
        return MemberProfile.builder()
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
