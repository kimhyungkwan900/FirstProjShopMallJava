package com.example.shop_mall_back.admin.order.service;

import com.example.shop_mall_back.admin.order.domain.ClaimManage;
import com.example.shop_mall_back.admin.order.dto.ClaimManageDto;
import com.example.shop_mall_back.admin.order.dto.ClaimSearchDto;
import com.example.shop_mall_back.admin.order.dto.ClaimUpdateDto;
import com.example.shop_mall_back.admin.order.dto.OrderReturnDto;
import com.example.shop_mall_back.admin.order.repository.ClaimManageRepository;
import com.example.shop_mall_back.user.myOrder.domain.OrderReturn;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

        System.out.println("서비스 객체 안에 들어옴: " + claimSearchDto);

        Page<ClaimManage> claimInfoPage = claimManageRepository.getClaimPageByCondition(claimSearchDto, pageable);

        return claimInfoPage.map(claimManage ->
                ClaimManageDto.builder()
                        .claimId(claimManage.getId())
                        .orderReturn(
                                OrderReturnDto.builder()
                                        .id(claimManage.getOrderReturn().getId())
                                        .orderId(claimManage.getOrderReturn().getOrderId())
                                        .memberId(claimManage.getOrderReturn().getMemberId())
                                        .returnType(claimManage.getOrderReturn().getReturnType())
                                        .reason(claimManage.getOrderReturn().getReason())
                                        .detail(claimManage.getOrderReturn().getDetail())
                                        .regDate(claimManage.getOrderReturn().getRegDate())
                                        .build()
                        )
                        .isApproved(null)
                        .build());
    }

    //고객 요청 승인여부 수정
    public void updateClaimApproval(ClaimUpdateDto claimUpdateDto){
        //프론트에서 받아온 claimUpdateDto의 claimId로 ClaimManage 테이블 탐색
        ClaimManage claimManage = claimManageRepository.findById(claimUpdateDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        String approval = claimUpdateDto.getApproval();
        OrderReturn.ReturnType current = claimManage.getOrderReturn().getReturnType();

        switch (current){
            case CANCEL_REQUEST:
                if ("승인".equals(approval)) {
                    System.out.println("취소 승인 안에 들어감");
                    current = OrderReturn.ReturnType.CANCEL_COMPLETE;
                } else {
                    current = OrderReturn.ReturnType.CANCEL_REJECTED;
                }
                break;
            case RETURN_REQUEST:
                if ("승인".equals(approval)) {
                    current = OrderReturn.ReturnType.RETURN_COMPLETE;
                } else {
                    current = OrderReturn.ReturnType.RETURN_REJECTED;
                }
                break;
            case EXCHANGE_REQUEST:
                if ("승인".equals(approval)) {
                    current = OrderReturn.ReturnType.EXCHANGE_COMPLETE;
                } else {
                    current = OrderReturn.ReturnType.EXCHANGE_REJECTED;
                }
                break;
        }

        claimManage.getOrderReturn().setReturnType(current);
    }
}
