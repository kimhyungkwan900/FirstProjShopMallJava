package com.example.shop_mall_back.user.Order.service;

import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberAddress;
import com.example.shop_mall_back.common.dto.MemberAddressDTO;
import com.example.shop_mall_back.common.repository.MemberAddressRepository;
import com.example.shop_mall_back.common.repository.MemberRepository;
import com.example.shop_mall_back.common.service.serviceimpl.MemberAddressServiceImpl;
import com.example.shop_mall_back.user.review.controller.ReviewController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 주문 화면에서 사용할 배송지 목록을 제공하는 서비스
 * - 사용자가 주문할 때 배송지를 선택할 수 있도록 도와줌
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberOrderAddressService {

    private final MemberAddressRepository memberAddressRepository;  // 배송지 데이터를 조회하는 Repository
    private final MemberRepository memberRepository;

    /**
     * 특정 회원의 배송지 목록을 조회하여 DTO 형태로 반환
     *
     * @param memberId 사용자 ID
     * @return 해당 사용자의 배송지 목록 (DTO 리스트)
     */
    public List<MemberAddressDTO> getMemberAddressList(Long memberId) {
        // 1. 회원이 존재하는지 먼저 검증
        memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 2. 회원 ID로 배송지 목록을 모두 조회
        List<MemberAddress> list = memberAddressRepository.findAllByMemberId(memberId);

        // 3. 배송지가 없으면 예외 처리
        if(list.isEmpty()){
            throw new IllegalArgumentException("등록된 배송지가 없습니다.");
        }

        // 4. 엔티티 → DTO 변환 후 반환
        return list.stream()
                .map(MemberAddressDTO::fromEntity) // 각 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }
}
