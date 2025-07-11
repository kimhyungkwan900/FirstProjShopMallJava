package com.example.shop_mall_back.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@NoArgsConstructor  //JSON 객체 변환시 기본생성자 필요한 경우 대비
@AllArgsConstructor
@Getter
@ToString(exclude = "userPassword")
public class MemberFormDTO {

    private Long id;

    @NotBlank
    @Length(min = 6, max = 20)
    private String userId;

    @NotBlank
    @Length(min = 6, max = 20)
    private String userPassword;

    @NotBlank
    @Email
    @Length(max = 40)
    private String email;

    @NotBlank
    @Length(max = 15)
    private String phoneNumber;
}
