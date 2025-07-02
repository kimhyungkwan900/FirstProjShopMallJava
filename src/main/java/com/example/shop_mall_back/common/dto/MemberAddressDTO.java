package com.example.shop_mall_back.common.dto;

import com.example.shop_mall_back.common.domain.member.MemberAddress;
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

    /**
     * MemberAddress 엔티티 객체를 MemberAddressDTO로 변환하는 정적 메서드
     * - 계층 간 데이터 전달을 위해 엔티티 → DTO로 매핑할 때 사용
     */
    public static MemberAddressDTO fromEntity(MemberAddress address) {
        return MemberAddressDTO.builder()
                .id(address.getId())                            // 배송지 ID
                .memberId(address.getMember().getId())          // 사용자 ID
                .zipcode(address.getZipcode())                  // 우편번호
                .address(address.getAddress())                  // 기본 주소
                .addressDetail(address.getAddressDetail())      // 상세 주소
                .note(address.getNote())                        // 배송 시 요청사항 메모
                .isDefault(address.isDefault())                 // 기본 배송지 여부
                .build();
    }
}
