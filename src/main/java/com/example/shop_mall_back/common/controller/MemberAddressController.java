package com.example.shop_mall_back.common.controller;

import com.example.shop_mall_back.common.config.CustomUserPrincipal;
import com.example.shop_mall_back.common.dto.MemberAddressDTO;
import com.example.shop_mall_back.common.service.serviceinterface.MemberAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberAddressController {

    private final MemberAddressService memberAddressService;

    @GetMapping("/addresses")
    public ResponseEntity<List<MemberAddressDTO>> getMyAddresses(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long memberId = principal.getMember().getId();

        List<MemberAddressDTO> addresses = memberAddressService.getMemberAddressList(memberId);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping("/addresses")
    public ResponseEntity<Void> addAddress(@RequestBody MemberAddressDTO memberAddressDTO, Authentication auth) {
        CustomUserPrincipal principal = (CustomUserPrincipal) auth.getPrincipal();
        Long memberId = principal.getMember().getId();

        memberAddressService.addMemberAddress(memberAddressDTO, memberId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/addresses")
    public ResponseEntity<Void> updateAddress(@RequestBody MemberAddressDTO memberAddressDTO, Authentication auth) {
        CustomUserPrincipal principal = (CustomUserPrincipal) auth.getPrincipal();
        Long memberId = principal.getMember().getId();

        memberAddressService.memberAddressUpdate(memberAddressDTO, memberId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id, Authentication auth) {
        CustomUserPrincipal principal = (CustomUserPrincipal) auth.getPrincipal();
        Long memberId = principal.getMember().getId();

        memberAddressService.memberAddressDelete(id, memberId);
        return ResponseEntity.noContent().build();
    }
}
