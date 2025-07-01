package com.example.shop_mall_back.common.service.oauthService;

import com.example.shop_mall_back.common.constant.*;
import com.example.shop_mall_back.common.domain.Member;
import com.example.shop_mall_back.common.domain.MemberProfile;
import com.example.shop_mall_back.common.repository.MemberProfileRepository;
import com.example.shop_mall_back.common.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String providerId = (String) attributes.get("sub");
        String profileImg = (String) attributes.get("picture");
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> {
                    Member newMember = Member.createOAuth2User(email,OauthProvider.GOOGLE,providerId);
                    return memberRepository.save(newMember);
                });

        boolean hasProfile = memberProfileRepository.existsByMemberId(member.getId());

        if (!hasProfile) {
            MemberProfile memberProfile = MemberProfile.builder()
                    .member(member)
                    .name(name)
                    .profileImgUrl(profileImg)
                    .role(Role.MEMBER)
                    .grade(Grade.NORMAL)
                    .gender(Gender.UNKNOWN)
                    .age(Age.UNKNOWN)
                    .nickname(name)
                    .isMembership(false)
                    .delivAddress(null)
                    .build();

            memberProfileRepository.save(memberProfile);
        }


        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "sub"
        );
    }
}
