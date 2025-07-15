package com.example.shop_mall_back.admin.order.repository;

import com.example.shop_mall_back.admin.order.domain.OrderManage;
import com.example.shop_mall_back.admin.order.domain.QOrderManage;
import com.example.shop_mall_back.admin.order.dto.OrderSearchDto;
import com.example.shop_mall_back.admin.product.dto.ProductSearchDto;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.common.domain.QProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class OrderManageRepositoryCustomImpl implements OrderManageRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrderManage> getOrderPageByCondition(OrderSearchDto orderSearchDto, Pageable pageable) {
        QOrderManage orderManage = QOrderManage.orderManage;    //자동 생성된 Q클래스

        System.out.println("여기에 들어오긴 하니");

        //동적 where 절 조립
        BooleanBuilder builder = new BooleanBuilder();

        System.out.println("searchType임: " + orderSearchDto.getSearchType());
        System.out.println("searchContent임: " + orderSearchDto.getSearchContent());

        if(StringUtils.hasText(orderSearchDto.getSearchType())){

            switch (orderSearchDto.getSearchType()) {
                //주문 아이디로 검색
                case "주문 ID" -> {
                    System.out.println("주문ID로 들어옴");
                    builder.and(orderManage.order.id.eq(Long.valueOf(orderSearchDto.getSearchContent())));
                }

                //고객 ID
                case "고객 ID" -> {
                    System.out.println("고객ID로 들어옴");
                    builder.and(orderManage.order.member.userId.contains(orderSearchDto.getSearchContent()));
                }
            }
        }

        //주문 상태로 검색
        if(StringUtils.hasText(orderSearchDto.getOrderStatus())){
            System.out.println("주문상태로 들어옴");
            builder.and(orderManage.orderStatus.eq(OrderManage.OrderStatus.valueOf(orderSearchDto.getOrderStatus())));
        }

        //주문 일자로 검색
        if(orderSearchDto.getStartDate() != null && orderSearchDto.getEndDate() != null){
            System.out.println("주문일자로 들어옴");
            builder.and(orderManage.order.orderDate.between(orderSearchDto.getStartDate(), orderSearchDto.getEndDate()));
        }

        //쿼리 실행
        List<OrderManage> searchResult = queryFactory
                .selectFrom(orderManage)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderManage.id.asc())
                .fetch();

        Long total = queryFactory
                .select(orderManage.count())
                .from(orderManage)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(searchResult, pageable, total);
    }
}
