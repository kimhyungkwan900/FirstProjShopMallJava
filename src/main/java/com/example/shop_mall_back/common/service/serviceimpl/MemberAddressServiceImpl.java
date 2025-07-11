package com.example.shop_mall_back.common.service.serviceimpl;

import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberAddress;
import com.example.shop_mall_back.common.dto.MemberAddressDTO;
import com.example.shop_mall_back.common.repository.MemberAddressRepository;
import com.example.shop_mall_back.common.repository.MemberRepository;
import com.example.shop_mall_back.common.service.serviceinterface.MemberAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberAddressServiceImpl implements MemberAddressService {

    private final MemberRepository memberRepository;
    private final MemberAddressRepository memberAddressRepository;

    //<editor-fold desc="맴버 주소 추가">
    @Override
    public Long addMemberAddress(MemberAddressDTO memberAddressDTO, Long id) {
        // 유저 확인
        Member member = memberRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 첫번째 주소인지 확인 (기존 저장된주소의 개수가 0 이라면)
        boolean isFirstAdd = memberAddressRepository.countByMemberId(member.getId()) == 0;

        // 기본 주소지 추가
        if (memberAddressDTO.isDefault() || isFirstAdd) {
            memberAddressRepository.resetDefaultAddressByMemberId(member.getId());
        }

        // 새 address 객체에 넣을 isDefault 값 설정
        boolean isDefault = memberAddressDTO.isDefault() || isFirstAdd;

        // 유저 address 객체생성
        MemberAddress memberAddress = MemberAddress.addAddress(member, memberAddressDTO.getZipcode(), memberAddressDTO.getAddress(), memberAddressDTO.getAddressDetail(), isDefault, memberAddressDTO.getNote());
        
        // 생성된 address 저장
        memberAddressRepository.save(memberAddress);

        // 저장된 address ID 반환
        return memberAddress.getId();
    }
    //</editor-fold>

    //<editor-fold desc="멤버 주소 검색">
    @Override
    public List<MemberAddressDTO> getMemberAddressList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        List<MemberAddress> addressList = memberAddressRepository.findByMember(member);

        return addressList.stream()
                .map(MemberAddressDTO::fromEntity)
                .collect(Collectors.toList());
    }
    //</editor-fold>

    //<editor-fold desc="맴버 주소 수정">
    @Override
    public void memberAddressUpdate(MemberAddressDTO memberAddressDTO) {
        // Update 할 address 검색
        MemberAddress memberAddress = memberAddressRepository.findById(memberAddressDTO.getId()).orElseThrow(()-> new IllegalArgumentException("해당 유저의 주소가 존재하지 않습니다."));

        // isDefault 설정 확인
        if(memberAddressDTO.isDefault() && !memberAddress.isDefault()){
            memberAddressRepository.resetDefaultAddressByMemberId(memberAddress.getMember().getId());
        }
        
        // 새로 입력받은 주소 저장
        memberAddress.updateAddress(memberAddressDTO.getZipcode(), memberAddressDTO.getAddress(), memberAddressDTO.getAddressDetail(), memberAddressDTO.isDefault(), memberAddressDTO.getNote());

        // 업데이트 내역 저장
        memberAddressRepository.save(memberAddress);
    }
    //</editor-fold>

    //<editor-fold desc="멤버 주소 삭제">
    @Override
    public void memberAddressDelete(Long id) {
        MemberAddress memberAddress = memberAddressRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 유저의 주소가 존재하지 않습니다."));

        memberAddressRepository.delete(memberAddress);
    }
    //</editor-fold>
}
