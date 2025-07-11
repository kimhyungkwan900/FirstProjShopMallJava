package com.example.shop_mall_back.common.service.serviceimpl;

import com.example.shop_mall_back.common.constant.Role;
import com.example.shop_mall_back.common.domain.member.MemberProfile;
import com.example.shop_mall_back.common.dto.MemberProfileDTO;
import com.example.shop_mall_back.common.dto.MemberProfileUpdateDTO;
import com.example.shop_mall_back.common.repository.MemberProfileRepository;
import com.example.shop_mall_back.common.service.serviceinterface.MemberProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberProfileServiceImpl implements MemberProfileService {

    private final MemberProfileRepository memberProfileRepository;

//    <editor-fold desc="멤버 프로필 검색">
    @Override
    public MemberProfileDTO getMemberProfile(Long memberId) {
        // memberProfile 검색
        MemberProfile memberProfile = findByMemberIdOrThrow(memberId);
        
        // 동일 ID memberProfile 반환
        return entityToDTO(memberProfile);
    }

    @Override
    public Role getMemberProfileRole(Long memberId) {
        MemberProfile memberProfile = findByMemberIdOrThrow(memberId);

        return memberProfile.getRole();
    }
//    </editor-fold>

//    <editor-fold desc="프로필 업데이트">
    @Override
    public void memberProfileUpdate(MemberProfileUpdateDTO dto) {
        // memberProfile 검색
        MemberProfile memberProfile = findByMemberIdOrThrow(dto.getMemberId());

        // 입력받아 들어온 정보로 Profile Update
        memberProfile.updateProfile(
                dto.getNickname(),
                dto.getProfileImgUrl()
        );

        // Transactional 존재 명시적 세이브
        memberProfileRepository.save(memberProfile);
    }
//    </editor-fold>

//    <editor-fold desc="Optional null 처리")
    @Override
    public MemberProfile findByMemberIdOrThrow(Long memberId) {
        return Optional.ofNullable(memberProfileRepository.findByMemberId(memberId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 프로필입니다."));
    }
//    </editor-fold>
}
