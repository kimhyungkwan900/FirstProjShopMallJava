package com.example.shop_mall_back.common.controller;


import com.example.shop_mall_back.common.dto.MemberFormDTO;
import com.example.shop_mall_back.common.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid MemberFormDTO memberFormDTO){
        memberService.signUp(memberFormDTO, passwordEncoder);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}
