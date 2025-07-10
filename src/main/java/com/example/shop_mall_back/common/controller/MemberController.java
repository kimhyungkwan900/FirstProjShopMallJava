package com.example.shop_mall_back.common.controller;


import com.example.shop_mall_back.common.config.CustomUserPrincipal;
import com.example.shop_mall_back.common.config.jwt.TokenProvider;
import com.example.shop_mall_back.common.constant.Role;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.dto.MemberFormDTO;
import com.example.shop_mall_back.common.dto.MemberProfileDTO;
import com.example.shop_mall_back.common.dto.MemberProfileUpdateDTO;
import com.example.shop_mall_back.common.dto.PasswordChangeDTO;
import com.example.shop_mall_back.common.service.serviceinterface.MemberProfileService;
import com.example.shop_mall_back.common.service.serviceinterface.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Log4j2
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MemberProfileService memberProfileService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid MemberFormDTO memberFormDTO){
        log.info("회원가입 요청 도착: {}", memberFormDTO);
        memberService.signUp(memberFormDTO, passwordEncoder);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path savePath = Paths.get("uploads").resolve(fileName);

        try {
            Files.createDirectories(savePath.getParent());
            file.transferTo(savePath.toFile());
            String imageUrl = "/static/uploads/" + fileName; // 클라이언트에서 접근 가능한 경로
            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody MemberProfileUpdateDTO memberProfileUpdateDTO,
                                              @AuthenticationPrincipal CustomUserPrincipal principal) {
        if (!memberProfileUpdateDTO.getMemberId().equals(principal.getMember().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        memberProfileService.memberProfileUpdate(memberProfileUpdateDTO);

        Member updatedMember = memberService.findByIdOrThrow(memberProfileUpdateDTO.getMemberId());
        Role role = memberProfileService.getMemberProfileRole(updatedMember.getId());
        String newAccessToken = tokenProvider.generateAccessToken(updatedMember.getId(), updatedMember.getEmail(), role);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }


    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestBody PasswordChangeDTO passwordChangeDTO, @AuthenticationPrincipal CustomUserPrincipal principal) {
        if (!passwordChangeDTO.getId().equals(principal.getMember().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        memberService.passWordUpdate(passwordChangeDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

}
