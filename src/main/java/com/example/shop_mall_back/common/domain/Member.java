package com.example.shop_mall_back.common.domain;


import com.example.shop_mall_back.common.dto.MemberFormDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@ToString
@Configuration
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    private Long id;

    private String user_id;

    private String user_password;

    private String email;
    private boolean email_verified;
    private String email_auth_code;

    private String phone_number;
    private boolean phone_number_verified;
    private String phone_number_auth_code;

    private String oauth_provider;
    private String oauth_id;

    private boolean is_active;

    @CreatedDate
    private LocalDateTime created_at;

    public Member create(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder){
        Member member = new Member();
        this.user_id = memberFormDTO.getUser_id();
        this.user_password = passwordEncoder.encode(memberFormDTO.getUser_password());
        this.email = memberFormDTO.getEmail();
        this.phone_number = memberFormDTO.getPhone_number();

        this.email_verified = true;
        this.email_auth_code = UUID.randomUUID().toString();

        this.phone_number_verified = true;
        this.phone_number_auth_code = UUID.randomUUID().toString();

        this.oauth_provider = UUID.randomUUID().toString();
        this.oauth_id = UUID.randomUUID().toString();

        this.is_active = true;
        this.created_at = LocalDateTime.now();

        return member;
    }

    public void changePassword(String newPassword){
        this.user_password = newPassword;
    }

    public void deActivateMember(){
        this.is_active = false;
    }

    public void activateMember(){
        this.is_active = true;
    }
}
