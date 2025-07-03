package com.example.shop_mall_back.admin.product.repository;

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
public class AdminProductRepositoryCustomImpl implements AdminProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    //검색조건 입력: 상품번호 , 상품명, 브랜드명
    //검색조건 선택: 판매상태, 카테고리 대분류(category), 중분류, 소분류, 등록일자

    @Override
    public Page<Product> getProductPageByCondition(ProductSearchDto productSearchDto, Pageable pageable) {
        QProduct product = QProduct.product;    //자동 생성된 Q클래스

        //동적 where 절 조립
        //상품 아이디로 검색
        BooleanBuilder builder = new BooleanBuilder();
        if(StringUtils.hasText(String.valueOf(productSearchDto.getId()))){
            builder.and(product.id.eq(productSearchDto.getId()));
        }
        //상품명으로 검색
        if(StringUtils.hasText(productSearchDto.getProductName())){
            builder.and(product.name.like("%"+productSearchDto.getProductName()+"%"));
        }
        //브랜드명으로 검색
        if(StringUtils.hasText(productSearchDto.getBrandName())){
            builder.and(product.brand.name.like("%"+productSearchDto.getBrandName()+"%"));
        }
        //판매 상태로 검색
        if(StringUtils.hasText(productSearchDto.getSellStatus())){
            builder.and(product.sellStatus.eq(Product.SellStatus.valueOf(productSearchDto.getSellStatus())));
        }
        //카테고리 ID로 검색
        if(StringUtils.hasText(String.valueOf(productSearchDto.getCategoryID()))){
            builder.and(product.category.id.eq(productSearchDto.getCategoryID()));
        }
        //등록 일자로 검색
        if(productSearchDto.getDateType() != null && productSearchDto.getDateType().equals("등록일")){
            builder.and(product.regTime.between(productSearchDto.getStartDate(), productSearchDto.getEndDate()));
        }
        //수정일자로 검색
        if(productSearchDto.getDateType() != null  && productSearchDto.getDateType().equals("수정일")){
            builder.and(product.updateTime.between(productSearchDto.getStartDate(), productSearchDto.getEndDate()));
        }

        //쿼리 실행
        List<Product> searchResult = queryFactory
                .selectFrom(product)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(product.count())
                .from(product)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(searchResult, pageable, total);
    }
}
