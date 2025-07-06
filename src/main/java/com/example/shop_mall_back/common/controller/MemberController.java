package com.example.shop_mall_back.common.controller;


import com.example.shop_mall_back.common.dto.MemberFormDTO;
import com.example.shop_mall_back.common.service.serviceinterface.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Log4j2
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid MemberFormDTO memberFormDTO){
        log.info("회원가입 요청 도착: {}", memberFormDTO);
        memberService.signUp(memberFormDTO, passwordEncoder);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
