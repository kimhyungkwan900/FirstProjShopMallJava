package com.example.shop_mall_back.admin.order.controller;

import com.example.shop_mall_back.admin.order.dto.ClaimManageDto;
import com.example.shop_mall_back.admin.order.dto.ClaimManageListDto;
import com.example.shop_mall_back.admin.order.dto.ClaimSearchDto;
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
    public ResponseEntity<?> claimsManage(ClaimSearchDto claimSearchDto, @PathVariable("page") Optional<Integer> page){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<ClaimManageDto> claims = claimManageService.getClaimInfoPage(claimSearchDto, pageable);

        ClaimManageListDto claimManageListDto = ClaimManageListDto.builder()
                .claims(claims)
                .claimSearchDto(claimSearchDto)
                .maxPage(10)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(claimManageListDto);
    }

    //---고객 요청 상세 조회
    @GetMapping("/claims/{claimId}")
    public ResponseEntity<ClaimManageDto> claimDetail(@PathVariable("claimId") Long claimId) {

        ClaimManageDto claimManageDto = claimManageService.getClaimDetail(claimId);

        return ResponseEntity.status(HttpStatus.OK).body(claimManageDto);
    }

    //---고객 요청 승인여부 수정
    @PatchMapping("/claims/approval")
    public ResponseEntity<?> approvalClaims(@RequestBody List<ClaimManageDto> claimList){
        for(ClaimManageDto claimManageDto : claimList){
            claimManageService.updateClaimApproval(claimManageDto);
        }

        return ResponseEntity.ok().build();
    }

}
