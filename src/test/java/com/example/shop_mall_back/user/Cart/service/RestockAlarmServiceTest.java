package com.example.shop_mall_back.user.Cart.service;

import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.user.Cart.domain.RestockAlarm;
import com.example.shop_mall_back.user.Cart.repository.RestockAlarmRepository;
import com.example.shop_mall_back.common.repository.MemberRepository;
import com.example.shop_mall_back.user.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito를 이용한 단위 테스트 실행을 위해 확장
class RestockAlarmServiceTest {

    @InjectMocks
    private RestockAlarmService restockAlarmService; // 테스트 대상 서비스

    @Mock
    private RestockAlarmRepository restockAlarmRepository; // 재입고 알림 레포지토리 Mock

    @Mock
    private MemberRepository memberRepository; // 회원 레포지토리 Mock

    @Mock
    private ProductRepository productRepository; // 상품 레포지토리 Mock

    /**
     * 재입고 알림 신청을 정상적으로 처리하는지 검증
     */
    @Test
    @DisplayName("재입고 알림 신청 - 성공")
    void requestRestockAlarm_success(){
        // given: 회원 ID와 상품 ID
        Long memberId = 1L;
        Long productId = 2L;

        Member member = Member.builder().id(memberId).build();
        Product product = Product.builder().id(productId).build();

        // Mock: 이미 신청된 알림이 없다고 가정
        when(restockAlarmRepository.existsByMember_IdAndProduct_Id(memberId, productId)).thenReturn(false);
        // Mock: 회원과 상품 조회 성공
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // when: 재입고 알림 신청 메소드 호출
        restockAlarmService.requestRestockAlarm(memberId, productId);

        // then: save() 메소드가 정확히 한 번 호출되었는지 검증
        verify(restockAlarmRepository, times(1)).save(any(RestockAlarm.class));
    }

    /**
     * 이미 알림이 신청된 경우 예외 발생 여부 검증
     */
    @Test
    @DisplayName("재입고 알림 신청 - 예외: 이미 신청된 경우")
    void requestRestockAlarm_alreadyExists(){
        // given: 회원 ID와 상품 ID
        Long memberId = 1L;
        Long productId = 2L;

        // Mock: 이미 신청된 알림이 존재한다고 가정
        when(restockAlarmRepository.existsByMember_IdAndProduct_Id(memberId, productId)).thenReturn(true);

        // when + then: IllegalStateException 발생 여부 검증
        assertThatThrownBy(() -> restockAlarmService.requestRestockAlarm(memberId, productId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 알림 신청이 되어 있습니다.");

        // then: save() 메소드가 절대 호출되지 않았는지 검증
        verify(restockAlarmRepository, never()).save(any(RestockAlarm.class));
    }

    /**
     * 상품이 존재하지 않을 때 예외 발생 여부 검증
     */
    @Test
    @DisplayName("재입고 알림 신청 - 예외: 상품이 존재하지 않음")
    void requestRestockAlarm_productNotFound(){
        // given: 회원 ID와 상품 ID
        Long memberId = 1L;
        Long productId = 2L;

        Member member = Member.builder().id(memberId).build();

        // Mock: 상품이 존재하지 않음
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // when + then: IllegalArgumentException 발생 여부 검증
        assertThatThrownBy(() -> restockAlarmService.requestRestockAlarm(memberId, productId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효한 상품이 아닙니다.");
    }

    /**
     * 회원이 존재하지 않을 때 예외 발생 여부 검증
     */
    @Test
    @DisplayName("재입고 알림 신청 - 예외: 회원이 존재하지 않음")
    void requestRestockAlarm_memberNotFound(){
        // given: 회원 ID와 상품 ID
        Long memberId = 1L;
        Long productId = 2L;

        Product product = Product.builder().id(productId).build();

        // Mock: 회원이 존재하지 않음
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // when + then: IllegalArgumentException 발생 여부 검증
        assertThatThrownBy(() -> restockAlarmService.requestRestockAlarm(memberId, productId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }
}
