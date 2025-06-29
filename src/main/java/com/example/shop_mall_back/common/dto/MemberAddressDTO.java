package com.example.shop_mall_back.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class MemberAddressDTO {

    private Long id;
    private Long memberId;

    @Length(max = 20)
    @NotBlank
    private String zipcode;

    @NotBlank
    private String address;

    @NotBlank
    @JsonProperty("address_detail") //JSON 응답개체에서 DB 컬럼명과 같은 이름을 가지도록 미리 설정
    private String addressDetail;

    private String note;

    @JsonProperty("is_default")
    private boolean isDefault;
}
