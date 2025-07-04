package com.example.shop_mall_back.admin.order.repository;

import com.example.shop_mall_back.admin.order.domain.ClaimManage;
import com.example.shop_mall_back.admin.order.domain.OrderManage;
import com.example.shop_mall_back.admin.order.domain.QClaimManage;
import com.example.shop_mall_back.admin.order.dto.ClaimSearchDto;
import com.example.shop_mall_back.user.Order.domain.OrderReturn;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ClaimManageRepositoryCustomImpl implements ClaimManageRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ClaimManage> getClaimPageByCondition(ClaimSearchDto claimSearchDto, Pageable pageable) {
        QClaimManage claimManage = QClaimManage.claimManage;

        //동적 where 절 조립
        BooleanBuilder builder = new BooleanBuilder();
        if(claimSearchDto.getSearchType() != null){
            switch (claimSearchDto.getSearchType()) {
                //주문 ID로 검색
                case "주문 ID" -> builder.and(claimManage.orderReturn.orderId.eq(Long.valueOf(claimSearchDto.getSearchContent())));

                //요청자 ID로 검색
                case "요청자 ID" -> {
                    builder.and(claimManage.orderReturn.memberId.eq(Long.valueOf(claimSearchDto.getSearchContent())));
                }
            }
        }

        //고객 요청 유형으로 검색
        if(claimSearchDto.getReturnType() != null){
            builder.and(claimManage.orderReturn.returnType.eq(OrderReturn.ReturnType.valueOf(claimSearchDto.getReturnType())));
        }

        //요청 일자로 검색
        if(claimSearchDto.getDateType() != null){
            builder.and(claimManage.orderReturn.regDate.between(claimSearchDto.getStartDate(), claimSearchDto.getEndDate()));
        }

        //쿼리 실행
        List<ClaimManage> searchResult = queryFactory
                .selectFrom(claimManage)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(claimManage.count())
                .from(claimManage)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(searchResult, pageable, total);
    }
}
