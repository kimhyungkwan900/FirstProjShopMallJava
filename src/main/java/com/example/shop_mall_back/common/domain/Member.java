package com.example.shop_mall_back.common.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Configuration
@EntityListeners(AuditingEntityListener.class)
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


}
