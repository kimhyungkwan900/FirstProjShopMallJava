package com.example.shop_mall_back.common.service.serviceinterface;

import com.example.shop_mall_back.common.constant.Role;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.dto.MemberDTO;
import com.example.shop_mall_back.common.dto.MemberFormDTO;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public interface MemberService {

    Long signUp(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder);

    MemberDTO getMemberDTOByEmail(String email);

    Optional<Member> findByUserId(String userId);

    Member findByEmail(String email);

    Role getRoleByMember(Member member);

    Member authenticate(String userId, String rawPassword);

    void memberFormUpdate(MemberFormDTO memberFormDTO,PasswordEncoder passwordEncoder);

    void deActivateMember(Long id);

    void activateMember(Long id);

    default MemberFormDTO entityToDTOMemberForm(Member member) {
        return MemberFormDTO.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .userPassword(member.getUserPassword())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .build();
    }

    // Member -> MemberDTO
    default MemberDTO entityToDTOMember(Member member){
        return MemberDTO.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .email(member.getEmail())
                .emailVerified(member.isEmailVerified())
                .phoneNumber(member.getPhoneNumber())
                .phoneNumberVerified(member.isPhoneVerified())
                .oauthProvider(member.getOauthProvider())
                .isActive(member.isActive())
                .createdAt(member.getCreatedAt())
                .build();
    }

    default Member dtoToEntity(MemberFormDTO memberFormDTO) {
        return Member.builder()
                .id(memberFormDTO.getId())
                .userId(memberFormDTO.getUserId())
                .userPassword(memberFormDTO.getUserPassword())
                .email(memberFormDTO.getEmail())
                .phoneNumber(memberFormDTO.getPhoneNumber())
                .build();
    }
}
