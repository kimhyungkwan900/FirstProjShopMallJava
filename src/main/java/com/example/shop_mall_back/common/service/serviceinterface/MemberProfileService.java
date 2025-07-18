package com.example.shop_mall_back.common.service.serviceinterface;

import com.example.shop_mall_back.common.constant.Role;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberProfile;
import com.example.shop_mall_back.common.dto.MemberProfileDTO;
import com.example.shop_mall_back.common.dto.MemberProfileUpdateDTO;

public interface MemberProfileService {

    MemberProfileDTO getMemberProfile(Long memberId);

    Role getMemberProfileRole(Long memberId);

    void memberProfileUpdate(MemberProfileUpdateDTO memberProfileUpdateDTO);

    MemberProfile findByMemberIdOrThrow(Long memberId);

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
                .build();
    }
}
