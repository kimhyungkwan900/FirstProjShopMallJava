package com.example.shop_mall_back.common.service.serviceinterface;

import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberAddress;
import com.example.shop_mall_back.common.dto.MemberAddressDTO;

import java.util.List;

public interface MemberAddressService {

    Long addMemberAddress(MemberAddressDTO memberAddressDTO, Long id);

    List<MemberAddressDTO> getMemberAddressList(Long memberId);

    void memberAddressUpdate(MemberAddressDTO memberAddressDTO, Long memberId);

    void memberAddressDelete(Long id, Long memberId);

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
