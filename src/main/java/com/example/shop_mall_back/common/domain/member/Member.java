package com.example.shop_mall_back.common.domain.member;


import com.example.shop_mall_back.common.constant.OauthProvider;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
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

    @Column(name = "user_id", nullable = false, length = 16, unique = true)
    private String userId;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_provider")
    private OauthProvider oauthProvider;

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
                .emailVerified(false)
                .emailAuthCode(UUID.randomUUID().toString())
                .phoneNumber(phoneNumber)
                .phoneVerified(true)            //인증이 되었다 가정
                .phoneAuthCode(UUID.randomUUID().toString().substring(0, 6))
                .oauthProvider(OauthProvider.LOCAL)
                .oauthId("자체회원")
                .isActive(true)
                .build();
    }

    public static Member createOAuth2User(String phoneNumber, String email, OauthProvider oauthProvider, String providerId) {
        return Member.builder()
                .userId(email)
                .userPassword(null)
                .email(email)
                .emailVerified(true)
                .emailAuthCode(null)
                .phoneNumber(phoneNumber)
                .phoneVerified(false)
                .phoneAuthCode(null)
                .oauthProvider(oauthProvider)
                .oauthId(providerId)
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
