package com.example.shop_mall_back.common.service.serviceinterface;

import com.example.shop_mall_back.common.constant.Role;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberProfile;
import com.example.shop_mall_back.common.dto.MemberDTO;
import com.example.shop_mall_back.common.dto.MemberFormDTO;
import com.example.shop_mall_back.common.dto.PasswordChangeDTO;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public interface MemberService {

    Long signUp(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder);

    MemberDTO getMemberDTOByEmail(String email);

    Optional<Member> findByUserId(String userId);

    Member findByEmail(String email);

    Role getRoleByMember(Member member);

    Member authenticate(String userId, String rawPassword);

    Member findByIdOrThrow(Long id);

    void passWordUpdate(PasswordChangeDTO passwordChangeDTO, PasswordEncoder passwordEncoder);

    void deActivateMember(Long id);

    void activateMember(Long id);

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
}
