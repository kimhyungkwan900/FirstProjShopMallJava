package com.example.shop_mall_back.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTO {
    private Long memberId;
    private String accessToken;
    private String refreshToken;
    private String ipAddress;
    private LocalDateTime expiresAt;
}