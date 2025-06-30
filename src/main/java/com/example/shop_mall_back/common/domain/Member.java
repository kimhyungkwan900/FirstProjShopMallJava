package com.example.shop_mall_back.common.domain;


import com.example.shop_mall_back.common.dto.MemberFormDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "members")
@Builder
@Getter
@ToString(exclude = {"userPassword", "emailAuthCode", "phoneAuthCode"})
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // user 로그인 id
    @Column(name = "user_id", nullable = false, length = 16, unique = true)
    private String userId;

    // user 로그인 password
    @Column(name = "user_password", nullable = false, length = 100)
    private String userPassword;

    @Column(nullable = false, length = 40, unique = true)
    private String email;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Column(name = "email_auth_code")
    private String emailAuthCode;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "phone_verified")
    private boolean phoneVerified;

    @Column(name = "phone_auth_code")
    private String phoneAuthCode;

    @Column(name = "oauth_provider")
    private String oauthProvider;

    @Column(name = "oauth_id")
    private String oauthId;

    @Column(name = "is_active")
    private boolean isActive;

    @CreatedDate
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    //TODO: 고정값 구현 완료시 변경할 것
    public static Member create(String userId,String userPassword,String email, String phoneNumber, PasswordEncoder encoder) {
        return Member.builder()
                .userId(userId)
                .userPassword(encoder.encode(userPassword))
                .email(email)
                .emailVerified(true)
                .emailAuthCode(UUID.randomUUID().toString())
                .phoneNumber(phoneNumber)
                .phoneVerified(true)
                .phoneAuthCode(UUID.randomUUID().toString().substring(0, 6))
                .oauthProvider("kakao")
                .oauthId(UUID.randomUUID().toString())
                .isActive(true)
                .build();
    }

    public void changePassword(String newPassword) {
        this.userPassword = newPassword;
    }

    public void deactivateMember() {
        this.isActive = false;
    }

    public void activateMember() {
        this.isActive = true;
    }
}
