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
public class GoogleOAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final OAuthMemberService oAuthMemberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String providerId = (String) attributes.get("sub");
        String profileImg = (String) attributes.get("picture");
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        Member member = oAuthMemberService.findOrCreateMember("01012122121", "test@gmail.com", OauthProvider.GOOGLE, providerId);

        oAuthMemberService.createProfileIfNotExists(member, name, name, profileImg, null, null);


        return new CustomOAuth2User(
                attributes,
                member.getId(),
                member.getEmail(),
                Role.MEMBER,
                OauthProvider.GOOGLE
        );
    }
}
