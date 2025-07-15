package com.example.shop_mall_back.common.controller;


import com.example.shop_mall_back.common.config.CustomUserPrincipal;
import com.example.shop_mall_back.common.config.jwt.TokenProvider;
import com.example.shop_mall_back.common.constant.Role;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberAddress;
import com.example.shop_mall_back.common.dto.*;
import com.example.shop_mall_back.common.repository.MemberAddressRepository;
import com.example.shop_mall_back.common.repository.MemberRepository;
import com.example.shop_mall_back.common.service.serviceinterface.MemberAddressService;
import com.example.shop_mall_back.common.service.serviceinterface.MemberProfileService;
import com.example.shop_mall_back.common.service.serviceinterface.MemberService;
import com.example.shop_mall_back.common.utils.CookieConstants;
import com.example.shop_mall_back.common.utils.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final MemberProfileService memberProfileService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid MemberFormDTO memberFormDTO){
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
    public ResponseEntity<?> updateProfile(@RequestBody MemberProfileUpdateDTO dto,
                                           @AuthenticationPrincipal CustomUserPrincipal principal,
                                           HttpServletResponse response) {
        if (!dto.getMemberId().equals(principal.getMember().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        memberProfileService.memberProfileUpdate(dto);

        Member updatedMember = memberService.findByIdOrThrow(dto.getMemberId());
        Role role = memberProfileService.getMemberProfileRole(updatedMember.getId());
        String newAccessToken = tokenProvider.generateAccessToken(updatedMember.getId(), updatedMember.getEmail(), role);

        CookieUtils.addCookie(response, CookieConstants.ACCESS_TOKEN, newAccessToken, tokenProvider.getAccessTokenExpirySeconds());

        return ResponseEntity.ok(Map.of("message", "프로필이 수정되었습니다."));
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
