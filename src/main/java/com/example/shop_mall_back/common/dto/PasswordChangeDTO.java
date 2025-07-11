package com.example.shop_mall_back.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDTO {
    private Long id;
    private String currentPassword;
    private String newPassword;
}