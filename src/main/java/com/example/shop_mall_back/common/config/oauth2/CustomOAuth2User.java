package com.example.shop_mall_back.common.config.oauth2;

import com.example.shop_mall_back.common.constant.OauthProvider;
import com.example.shop_mall_back.common.constant.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final Map<String, Object> attributes;
    private final Long id;
    private final String email;
    private final Role role;
    private final OauthProvider provider;


    public CustomOAuth2User(Map<String, Object> attributes, Long id, String email, Role role, OauthProvider provider) {
        this.attributes = attributes;
        this.id = id;
        this.email = email;
        this.role = role;
        this.provider = provider;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
