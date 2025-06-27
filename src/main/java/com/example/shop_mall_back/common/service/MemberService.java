package com.example.shop_mall_back.common.service;

import com.example.shop_mall_back.common.domain.Member;
import com.example.shop_mall_back.common.dto.MemberFormDTO;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface MemberService {

    Long signUp(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder);

    MemberFormDTO getMemberForm(String email);

    void memberFormUpdate(MemberFormDTO memberFormDTO,PasswordEncoder passwordEncoder);

    void deActivateMember(Long id);

    void activateMember(Long id);

    default MemberFormDTO entityToDTO(Member member) {
        return MemberFormDTO.builder()
                .id(member.getId())
                .user_id(member.getUser_id())
                .user_password(member.getUser_password())
                .email(member.getEmail())
                .phone_number(member.getPhone_number())
                .build();
    }

    default Member dtoToEntity(MemberFormDTO memberFormDTO) {
        return Member.builder()
                .id(memberFormDTO.getId())
                .user_id(memberFormDTO.getUser_id())
                .user_password(memberFormDTO.getUser_password())
                .email(memberFormDTO.getEmail())
                .phone_number(memberFormDTO.getPhone_number())
                .build();
    }
}
