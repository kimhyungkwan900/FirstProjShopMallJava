package com.example.shop_mall_back.common.service;

import com.example.shop_mall_back.common.domain.Member;
import com.example.shop_mall_back.common.domain.MemberAddress;
import com.example.shop_mall_back.common.dto.MemberAddressDTO;

public interface MemberAddressService {

    Long addMemberAddress(MemberAddressDTO memberAddressDTO, Long id);

    MemberAddressDTO getMemberAddress(Long id);

    void memberAddressUpdate(MemberAddressDTO memberAddressDTO);

    void memberAddressDelete(Long id);

    default MemberAddressDTO entityToDTO(MemberAddress memberAddress) {
        return MemberAddressDTO.builder()
                .id(memberAddress.getId())
                .memberId(memberAddress.getMember().getId())
                .zipcode(memberAddress.getZipcode())
                .address(memberAddress.getAddress())
                .addressDetail(memberAddress.getAddressDetail())
                .note(memberAddress.getNote())
                .isDefault(memberAddress.isDefault())
                .build();
    }

    default MemberAddress dtoToEntity(MemberAddressDTO memberAddressDTO, Member member) {
        return MemberAddress.builder()
                .id(memberAddressDTO.getId())
                .member(member)
                .zipcode(memberAddressDTO.getZipcode())
                .address(memberAddressDTO.getAddress())
                .addressDetail(memberAddressDTO.getAddressDetail())
                .note(memberAddressDTO.getNote())
                .isDefault(memberAddressDTO.isDefault())
                .build();
    }
}
