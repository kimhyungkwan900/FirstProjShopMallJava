package com.example.shop_mall_back.admin.order.service;

import com.example.shop_mall_back.admin.order.dto.ClaimManageDto;
import com.example.shop_mall_back.admin.order.dto.ClaimSearchDto;
import com.example.shop_mall_back.admin.order.repository.ClaimManageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClaimManageService {

    private final ClaimManageRepository claimManageRepository;
    private final ModelMapper modelMapper;

    //검색 조건에 따라 고객 요청 조회
    @Transactional(readOnly = true)
    public Page<ClaimManageDto> getClaimInfoPage(ClaimSearchDto claimSearchDto, Pageable pageable) {
        return claimManageRepository.getClaimPageByCondition(claimSearchDto, pageable)
                .map(claimManage -> modelMapper.map(claimManage, ClaimManageDto.class));
    }

    //고객 요청 상세 조회
    @Transactional
    public ClaimManageDto getClaimDetail(Long claimId){
        return modelMapper.map(claimManageRepository.findById(claimId), ClaimManageDto.class);
    }

    //고객 요청 승인여부 수정
    public void updateClaimApproval(){

    }

}
