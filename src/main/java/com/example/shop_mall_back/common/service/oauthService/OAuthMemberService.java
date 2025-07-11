package com.example.shop_mall_back.common.service.oauthService;

import com.example.shop_mall_back.common.constant.*;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberProfile;
import com.example.shop_mall_back.common.repository.MemberProfileRepository;
import com.example.shop_mall_back.common.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthMemberService {

    private final MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;

    public Member findOrCreateMember(String phoneNumber, String email, OauthProvider provider, String providerId) {
        return memberRepository.findByEmail(email)
                .orElseGet(() -> {
                   Member newMember = Member.createOAuth2User(phoneNumber, email, provider, providerId);
                   return memberRepository.save(newMember);
                });
    }

    public void createProfileIfNotExists(Member member, String name, String nickname, String profileImg, Gender gender, Age age) {
        boolean hasProfile = memberProfileRepository.existsByMemberId(member.getId());

        if(name == null || name.isEmpty()) {
            name = "oauthUser";
        }
        if(gender == null){
            gender = Gender.UNKNOWN;
        }
        if (age == null){
            age = Age.UNKNOWN;
        }

        if(!hasProfile) {
            MemberProfile memberProfile = MemberProfile.builder()
                    .member(member)
                    .name(name)
                    .nickname(nickname)
                    .gender(gender)
                    .age(age)
                    .profileImgUrl(profileImg)
                    .role(Role.MEMBER)
                    .grade(Grade.NORMAL)
                    .isMembership(false)
                    .build();

            memberProfileRepository.save(memberProfile);
        }
    }
}
