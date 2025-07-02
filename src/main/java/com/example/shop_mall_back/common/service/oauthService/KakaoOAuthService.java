package com.example.shop_mall_back.common.service.oauthService;

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

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final OAuthMemberService oAuthMemberService;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String providerId = String.valueOf(attributes.get("id"));
        String email = (String) kakaoAccount.get("email");
        String name = (String) kakaoAccount.get("nickname");
        String phoneNumber = (String) kakaoAccount.get("phone_number");
        String gender = (String) kakaoAccount.get("gender");
        String ageRange = (String) kakaoAccount.get("age_range");
        String nickname = (String) profile.get("nickname");
        String profileImg = (String) profile.get("profile_image_url");

        Gender gen = Gender.conversion(gender);
        Age age = Age.conversion(ageRange);

        Member member = oAuthMemberService.findOrCreateMember(phoneNumber, email, OauthProvider.KAKAO, providerId);

        oAuthMemberService.createProfileIfNotExists(member, name, nickname, profileImg, gen, age);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                "id"
        );
    }
}
