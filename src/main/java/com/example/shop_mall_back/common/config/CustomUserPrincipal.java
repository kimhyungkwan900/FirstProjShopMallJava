package com.example.shop_mall_back.common.config;

import com.example.shop_mall_back.common.constant.Role;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberProfile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CustomUserPrincipal implements UserDetails, OAuth2User {

    private final Member member;
    private final MemberProfile profile;
    private final Map<String, Object> attributes; // 소셜 로그인 전용

    public CustomUserPrincipal(Member member, MemberProfile profile) {
        this.member = member;
        this.profile = profile;
        this.attributes = Collections.emptyMap();
    }

    public CustomUserPrincipal(Member member, MemberProfile profile, Map<String, Object> attributes) {
        this.member = member;
        this.profile = profile;
        this.attributes = attributes;
    }

    // UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + profile.getRole().name()));
    }

    // 기본 정보
    @Override public String getUsername() { return member.getEmail(); }
    @Override public String getPassword() { return member.getUserPassword(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    // OAuth2
    @Override public Map<String, Object> getAttributes() { return attributes; }
    @Override public String getName() { return String.valueOf(member.getId()); }

    // 사용자 정보 접근용 getter
    public Member getMember() { return member; }
    public MemberProfile getProfile() { return profile; }

    public Role getRole() { return profile.getRole(); }
}
