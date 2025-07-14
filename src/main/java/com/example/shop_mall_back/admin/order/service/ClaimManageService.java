package com.example.shop_mall_back.admin.order.service;

import com.example.shop_mall_back.admin.order.domain.ClaimManage;
import com.example.shop_mall_back.admin.order.dto.ClaimManageDto;
import com.example.shop_mall_back.admin.order.dto.ClaimSearchDto;
import com.example.shop_mall_back.admin.order.repository.ClaimManageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.shop_mall_back.user.myOrder.domain.OrderReturn.ReturnType;

@Service
@Transactional
@RequiredArgsConstructor
public class ClaimManageService {

    private final ClaimManageRepository claimManageRepository;
    private final ModelMapper modelMapper;

    //검색 조건에 따라 고객 요청 조회
    @Transactional(readOnly = true)
    public Page<ClaimManageDto> getClaimInfoPage(ClaimSearchDto claimSearchDto, Pageable pageable) {

        Page<ClaimManage> claimInfoPage = claimManageRepository.getClaimPageByCondition(claimSearchDto, pageable);

        return claimInfoPage.map(claimManage ->
                ClaimManageDto.builder()
                        .claimId(claimManage.getId())
                        .orderReturn(claimManage.getOrderReturn())
                        .build());
    }

    //고객 요청 승인여부 수정
    public void updateClaimApproval(ClaimManageDto claimManageDto){
        //프론트에서 받아온 ClaimManage의 claimId로 ClaimManage 테이블 탐색
        ClaimManage claimManage = claimManageRepository.findById(claimManageDto.getClaimId())
                .orElseThrow(EntityNotFoundException::new);

        String returnType = String.valueOf(claimManage.getOrderReturn().getReturnType());

        switch (returnType){
            case "CANCEL_REQUEST":
                if(claimManageDto.getIsApproved())
                    claimManage.getOrderReturn().setReturnType(ReturnType.CANCEL_COMPLETE);
                else
                    claimManage.getOrderReturn().setReturnType(ReturnType.CANCEL_REJECTED);
                break;
            case "RETURN_REQUEST":
                if(claimManageDto.getIsApproved())
                    claimManage.getOrderReturn().setReturnType(ReturnType.RETURN_COMPLETE);
                else
                    claimManage.getOrderReturn().setReturnType(ReturnType.RETURN_REJECTED);
                break;
            case "EXCHANGE_REQUEST":
                if(claimManageDto.getIsApproved())
                    claimManage.getOrderReturn().setReturnType(ReturnType.EXCHANGE_COMPLETE);
                else
                    claimManage.getOrderReturn().setReturnType(ReturnType.EXCHANGE_REJECTED);
                break;
        }

        claimManageRepository.save(claimManage);
    }
}
