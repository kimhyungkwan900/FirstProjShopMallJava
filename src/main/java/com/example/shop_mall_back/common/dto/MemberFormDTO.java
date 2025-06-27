package com.example.shop_mall_back.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@ToString
public class MemberFormDTO {

    private Long id;

    @NotBlank
    @Length(min = 6, max = 20)
    private String user_id;

    @NotBlank
    @Length(min = 6, max = 20)
    private String user_password;

    @NotBlank
    @Email
    @Length(max = 40)
    private String email;

    @NotBlank
    @Length(max = 15)
    private String phone_number;
}
