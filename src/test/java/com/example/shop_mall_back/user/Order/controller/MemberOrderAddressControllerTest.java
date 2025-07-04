package com.example.shop_mall_back.user.Order.controller;

import com.example.shop_mall_back.common.config.jwt.TokenProvider;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberAddress;
import com.example.shop_mall_back.common.dto.MemberAddressDTO;
import com.example.shop_mall_back.common.service.serviceimpl.MemberAddressServiceImpl;
import com.example.shop_mall_back.user.Order.service.MemberOrderAddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberOrderAddressController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberOrderAddressControllerTest {

    @MockBean
    private TokenProvider tokenProvider; // 🔑 JWT 인증 필터에서 주입 필요하지만, 여기선 사용 안 함

    @Autowired
    private MockMvc mockMvc; // 🧪 API 요청/응답 테스트에 사용

    @MockBean
    private MemberAddressServiceImpl memberAddressServiceImpl; // 🛠 배송지 추가/수정/삭제 서비스 Mock

    @MockBean
    private MemberOrderAddressService memberOrderAddressService; // 🛠 배송지 조회 서비스 Mock

    /**
     * ✅ 배송지 목록 조회 - 성공 케이스
     */
    @Test
    @DisplayName("배송지 목록 조회 - 성공")
    public void findAllByMemberId_success() throws Exception {
        Long memberId = 1L;

        // 📝 테스트용 MemberAddress 객체 2개 생성
        MemberAddress memberAddress1 = MemberAddress.builder()
                .id(1L).member(Member.builder().id(memberId).build())
                .zipcode("12345").address("서울시 강남구").addressDetail("101동 202호")
                .note("문 앞에 두세요").isDefault(true).build();

        MemberAddress memberAddress2 = MemberAddress.builder()
                .id(2L).member(Member.builder().id(memberId).build())
                .zipcode("67890").address("부산 해운대구").addressDetail("303동 404호")
                .note("경비실에 맡겨주세요").isDefault(false).build();

        // MemberAddress → DTO 변환
        List<MemberAddressDTO> memberAddressDTO = List.of(
                MemberAddressDTO.fromEntity(memberAddress1),
                MemberAddressDTO.fromEntity(memberAddress2)
        );

        // 💡 Mock: 서비스 메서드 호출 시 반환값 지정
        when(memberOrderAddressService.getMemberAddressList(memberId))
                .thenReturn(memberAddressDTO);

        // ✅ GET 요청 수행 및 결과 검증
        mockMvc.perform(get("/api/order/addresses/list")
                        .param("memberId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].address").value("서울시 강남구"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].address").value("부산 해운대구"));
    }

    /**
     * ❌ 회원 없음 실패 케이스
     */
    @Test
    @DisplayName("배송지 목록 조회 - 실패: 회원 없음")
    void findAllByMemberId_memberNotFound() throws Exception {
        Long memberId = 99L;

        // 💥 회원 없음 예외 발생 설정
        when(memberOrderAddressService.getMemberAddressList(memberId))
                .thenThrow(new IllegalArgumentException("회원이 존재하지 않습니다."));

        // ✅ GET 요청 및 예외 메시지 검증
        mockMvc.perform(get("/api/order/addresses/list")
                        .param("memberId", memberId.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("회원이 존재하지 않습니다."));
    }

    /**
     * ✅ 배송지 추가 - 성공
     */
    @Test
    @DisplayName("배송지 추가 - 성공")
    void addAddress_success() throws Exception {
        Long memberId = 1L;

        MemberAddress memberAddress1 = MemberAddress.builder()
                .id(3L).member(Member.builder().id(memberId).build())
                .zipcode("12345").address("서울시 강남구").addressDetail("101동 202호")
                .note("문 앞에 두세요").isDefault(true).build();

        MemberAddressDTO memberAddressDTO = MemberAddressDTO.fromEntity(memberAddress1);

        // 💡 Mock: 서비스 호출 시 반환값 설정
        when(memberAddressServiceImpl.addMemberAddress(any(MemberAddressDTO.class), eq(memberId)))
                .thenReturn(memberAddressDTO.getId());

        // ✅ POST 요청 및 응답 검증
        mockMvc.perform(post("/api/order/addresses/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(memberAddressDTO)) // JSON 변환
                        .param("memberId", String.valueOf(memberId)))
                .andExpect(status().isOk())
                .andExpect(content().string("3")); // 반환값 검증
    }

    /**
     * ✅ 배송지 수정 - 성공
     */
    @Test
    @DisplayName("배송지 수정 - 성공")
    void updateAddress_success() throws Exception {
        Long memberId = 1L;

        MemberAddress memberAddress = MemberAddress.builder()
                .id(3L).member(Member.builder().id(memberId).build())
                .zipcode("54321").address("서울시 서초구").addressDetail("202동 303호")
                .note("벨 누르지 마세요").isDefault(false).build();

        MemberAddressDTO memberAddressDTO = MemberAddressDTO.fromEntity(memberAddress);

        // 🔥 서비스 메서드 void 반환이라 doNothing 사용
        doNothing().when(memberAddressServiceImpl).memberAddressUpdate(memberAddressDTO);

        // ✅ PUT 요청 및 상태 검증
        mockMvc.perform(put("/api/order/addresses/{addressId}/update", "3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(memberAddressDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    /**
     * ✅ 배송지 삭제 - 성공
     */
    @Test
    @DisplayName("배송지 삭제 - 성공")
    void deleteAddress_success() throws Exception {
        Long addressId = 3L;

        // 🔥 서비스 메서드 void 반환이라 doNothing 사용
        doNothing().when(memberAddressServiceImpl).memberAddressDelete(addressId);

        // ✅ DELETE 요청 및 상태 검증
        mockMvc.perform(delete("/api/order/addresses/{addressId}/delete", addressId))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
