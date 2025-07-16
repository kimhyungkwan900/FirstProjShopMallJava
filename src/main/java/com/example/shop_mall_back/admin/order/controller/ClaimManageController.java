package com.example.shop_mall_back.admin.order.controller;

import com.example.shop_mall_back.admin.order.dto.ClaimManageDto;
import com.example.shop_mall_back.admin.order.dto.ClaimManageListDto;
import com.example.shop_mall_back.admin.order.dto.ClaimSearchDto;
import com.example.shop_mall_back.admin.order.dto.ClaimUpdateDto;
import com.example.shop_mall_back.admin.order.service.ClaimManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class ClaimManageController {
    private final ClaimManageService claimManageService;

    //---조회 조건과 페이지 정보를 받아서 고객요청 데이터 조회
    @GetMapping({"/claims", "/claims/{page}"})
    public ResponseEntity<?> claimsManage(@ModelAttribute ClaimSearchDto claimSearchDto, @RequestParam(value="page", defaultValue = "0") int page){

        System.out.println("검색조건: " + claimSearchDto);

        Pageable pageable = PageRequest.of(page, 8);
        Page<ClaimManageDto> claims = claimManageService.getClaimInfoPage(claimSearchDto, pageable);

        System.out.println("서비스 객체에서 나왔음");

        ClaimManageListDto claimManageListDto = ClaimManageListDto.builder()
                .claims(claims)
                .claimSearchDto(claimSearchDto)
                .maxPage(10)
                .totalPage(claims.getTotalPages())
                .build();

        System.out.println("claims: " + claims);
        return ResponseEntity.status(HttpStatus.OK).body(claimManageListDto);
    }

    //---고객 요청 승인여부 수정
    @PatchMapping("/claims/status")
    public ResponseEntity<?> approvalClaims(@RequestBody ClaimUpdateDto claimUpdateDto){
        claimManageService.updateClaimApproval(claimUpdateDto);

        return ResponseEntity.ok().build();
    }

}
