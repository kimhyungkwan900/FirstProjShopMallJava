package com.example.shop_mall_back.user.Order.service;

import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberAddress;
import com.example.shop_mall_back.common.dto.MemberAddressDTO;
import com.example.shop_mall_back.common.repository.MemberAddressRepository;
import com.example.shop_mall_back.common.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito 기반 단위 테스트 실행
class MemberOrderAddressServiceTest {

    @InjectMocks
    private MemberOrderAddressService memberOrderAddressService; // 테스트 대상 서비스

    @Mock
    MemberAddressRepository memberAddressRepository; // 회원 배송지 레포지토리 Mock

    @Mock
    private MemberRepository memberRepository; // 회원 레포지토리 Mock

    /**
     * [성공 시나리오]
     * 특정 회원의 배송지 목록을 정상적으로 조회할 수 있는지 검증
     */
    @Test
    @DisplayName("회원 배송지 목록 조회 - 성공")
    public void getMemberAddressList_success()  {
        // given: 회원과 해당 회원의 배송지 2개를 준비
        Long memberId = 1L;
        Member member = Member.builder().id(memberId).build();
        MemberAddress address1 = MemberAddress.builder().id(101L).member(member).address("서울 강남구").build();
        MemberAddress address2 = MemberAddress.builder().id(102L).member(member).address("부산 해운대구").build();

        // Mock: 회원 조회 및 배송지 목록 반환
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberAddressRepository.findAllByMemberId(memberId)).thenReturn(List.of(address1, address2));

        // when: 서비스 메소드 호출
        List<MemberAddressDTO> result = memberOrderAddressService.getMemberAddressList(memberId);

        // then: 반환된 리스트 크기 및 데이터 검증
        assertThat(result).hasSize(2); // 배송지 2건 반환 확인
        assertThat(result.get(0).getAddress()).isEqualTo("서울 강남구"); // 첫 번째 주소 확인
        assertThat(result.get(1).getAddress()).isEqualTo("부산 해운대구"); // 두 번째 주소 확인

        // 레포지토리 호출 여부 검증
        verify(memberRepository).findById(memberId);
        verify(memberAddressRepository).findAllByMemberId(memberId);
    }

    /**
     * [예외 시나리오]
     * 회원이 존재하지 않을 경우 IllegalArgumentException이 발생하는지 검증
     */
    @Test
    @DisplayName("회원 배송지 목록 조회 - 예외 : 회원이 존재하지 않음")
    void getMemberAddressList_memberNotFound(){
        // given: 존재하지 않는 회원 ID
        Long memberId = 1L;

        // Mock: 회원이 존재하지 않음
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when + then: IllegalArgumentException 발생 여부 및 메시지 검증
        assertThatThrownBy(() -> memberOrderAddressService.getMemberAddressList(memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원이 존재하지 않습니다.");
    }

    /**
     * [예외 시나리오]
     * 회원은 존재하지만 등록된 배송지가 없는 경우 IllegalArgumentException이 발생하는지 검증
     */
    @Test
    @DisplayName("회원 배송지 목록 조회 - 예외 : 배송지가 없음")
    void getMemberAddressList_noAddresses(){
        // given: 존재하는 회원 ID
        Long memberId = 1L;
        Member member = Member.builder().id(memberId).build();

        // Mock: 회원 조회 성공, 배송지 목록은 비어 있음
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberAddressRepository.findAllByMemberId(memberId)).thenReturn(List.of());

        // when + then: IllegalArgumentException 발생 여부 및 메시지 검증
        assertThatThrownBy(() -> memberOrderAddressService.getMemberAddressList(memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록된 배송지가 없습니다.");
    }
}
