package com.example.shop_mall_back.user.Order.controller;

import com.example.shop_mall_back.common.dto.MemberAddressDTO;
import com.example.shop_mall_back.common.service.serviceimpl.MemberAddressServiceImpl;
import com.example.shop_mall_back.user.Order.service.MemberOrderAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 주문 과정에서 사용하는 배송지 컨트롤러
 * - 회원이 주문 전에 배송지를 선택하거나 새로 추가/수정/삭제할 수 있도록 제공
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order/addresses")
public class MemberOrderAddressController {

    private final MemberAddressServiceImpl memberAddressServiceImpl;       // 배송지 추가/수정/삭제를 담당하는 서비스
    private final MemberOrderAddressService memberOrderAddressService;     // 배송지 조회를 담당하는 서비스

    /**
     * [1] 배송지 목록 조회
     * GET /api/order/addresses/list?memberId=1
     *
     * @param memberId 사용자 ID
     * @return 해당 사용자의 배송지 목록
     */
    @GetMapping("/list")
    public ResponseEntity<List<MemberAddressDTO>> findAllByMemberId(@RequestParam Long memberId) {
        List<MemberAddressDTO> list = memberOrderAddressService.getMemberAddressList(memberId);
        return ResponseEntity.ok(list);
    }
    // 👇 추가: IllegalArgumentException 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    /**
     * [2] 배송지 추가
     * POST /api/order/addresses/add?memberId=1
     *
     * @param memberAddressDTO 추가할 배송지 정보 (RequestBody로 전달됨)
     * @param memberId 사용자 ID (RequestParam으로 전달됨)
     * @return 생성된 배송지 ID
     */
    @PostMapping("/add")
    public ResponseEntity<Long> addAddress(@RequestBody MemberAddressDTO memberAddressDTO,
                                           @RequestParam Long memberId) {
        Long id = memberAddressServiceImpl.addMemberAddress(memberAddressDTO, memberId);
        return ResponseEntity.ok(id);
    }

    /**
     * [3] 배송지 수정
     * PUT /api/order/addresses/{addressId}/update
     *
     * @param memberAddressDTO 수정할 배송지 정보 (ID 포함)
     * @return 200 OK (응답 바디 없음)
     */
    @PutMapping("/{addressId}/update")
    public ResponseEntity<Void> updateAddress(@RequestBody MemberAddressDTO memberAddressDTO) {
        memberAddressServiceImpl.memberAddressUpdate(memberAddressDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * [4] 배송지 삭제
     * DELETE /api/order/addresses/{addressId}/delete
     *
     * @param addressId 삭제할 배송지 ID
     * @return 200 OK (응답 바디 없음)
     */
    @DeleteMapping("/{addressId}/delete")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        memberAddressServiceImpl.memberAddressDelete(addressId);
        return ResponseEntity.ok().build();
    }
}
