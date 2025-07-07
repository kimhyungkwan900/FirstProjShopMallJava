package com.example.shop_mall_back.common.service.oauthService;

import com.example.shop_mall_back.common.config.oauth2.CustomOAuth2User;
import com.example.shop_mall_back.common.constant.*;
import com.example.shop_mall_back.common.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
public class NaverOAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final OAuthMemberService oAuthMemberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        String providerId  = (String) response.get("id");
        String name = (String) response.get("name");
        String email = (String) response.get("email");
        String phoneNumber = (String) response.get("mobile");
        String nickname = (String) response.get("nickname");
        String profileImg = (String) response.get("profile_image");
        String gender = (String) response.get("gender");
        String ageRange = (String) response.get("age");

        Gender gen = Gender.conversion(gender);
        Age age = Age.conversion(ageRange);


        Member member = oAuthMemberService.findOrCreateMember("01022221111", "test@naver.com", OauthProvider.NAVER, providerId);
        oAuthMemberService.createProfileIfNotExists(member, name, nickname, profileImg, gen, age);

        return new CustomOAuth2User(
                attributes,
                member.getId(),
                member.getEmail(),
                Role.MEMBER
        );
    }
}
