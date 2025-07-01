package com.example.shop_mall_back.common.service;

import com.example.shop_mall_back.common.domain.Member;
import com.example.shop_mall_back.common.dto.MemberFormDTO;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface MemberService {

    Long signUp(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder);

    Long oAuth2SignUp(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder);

    MemberFormDTO getMemberForm(String email);

    void memberFormUpdate(MemberFormDTO memberFormDTO,PasswordEncoder passwordEncoder);

    void deActivateMember(Long id);

    void activateMember(Long id);

    default MemberFormDTO entityToDTO(Member member) {
        return MemberFormDTO.builder()
                .id(member.getId())
                .user_id(member.getUserId())
                .user_password(member.getUserPassword())
                .email(member.getEmail())
                .phone_number(member.getPhoneNumber())
                .build();
    }

    default Member dtoToEntity(MemberFormDTO memberFormDTO) {
        return Member.builder()
                .id(memberFormDTO.getId())
                .userId(memberFormDTO.getUser_id())
                .userPassword(memberFormDTO.getUser_password())
                .email(memberFormDTO.getEmail())
                .phoneNumber(memberFormDTO.getPhone_number())
                .build();
    }
}
