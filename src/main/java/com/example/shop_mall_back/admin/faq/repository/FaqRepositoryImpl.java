package com.example.shop_mall_back.admin.faq.repository;

import com.example.shop_mall_back.admin.faq.domain.Faq;
import com.example.shop_mall_back.admin.faq.domain.QFaq;
import com.example.shop_mall_back.admin.faq.dto.FaqSearchDto;
import com.example.shop_mall_back.admin.faq.dto.PageRequestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;

import org.springframework.data.domain.Pageable;
import java.util.List;

@RequiredArgsConstructor
public class FaqRepositoryImpl implements FaqRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Faq> searchFaqs(FaqSearchDto faqSearchDto, Pageable pageable) {

        QFaq faq = QFaq.faq; //QueryDSL이 자동으로 만든 FAQ도메인

        //검색 조건을 담을 객체
        BooleanBuilder builder = new BooleanBuilder();

        //검색시에 카테고리 반드시 존재해야함
        if (!StringUtils.hasText(faqSearchDto.getCategory())) {
            return Page.empty();
        }
        builder.and(faq.category.eq(faqSearchDto.getCategory()));


        //키워드가 있으면 제목이나 답변에 포함되었는지 검색함
        if (StringUtils.hasText(faqSearchDto.getKeyWord())) {
            builder.and(
                    faq.question.containsIgnoreCase(faqSearchDto.getKeyWord())
                            .or(faq.answer.containsIgnoreCase(faqSearchDto.getKeyWord()))
            );
        }

        //실제 faq 목록 검색해오기
        List<Faq> faqList = queryFactory
                .select(faq) //faq 테이블에서
                .from(faq)
                .where(builder) //위에서 만든 조건들로 필터링해서
                .offset(pageable.getOffset()) //페이징 처리
                .limit(pageable.getPageSize()) //힌번에 몇개 보여줄지
                .orderBy(faq.createdAt.desc()) //최신순으로 정렬
                .fetch();

        Long total = queryFactory
                .select(faq.count())
                .from(faq)
                .where(builder)
                .fetchOne();

        total = total != null ? total : 0L;

        return new PageImpl<>(faqList, pageable, total);
    }

}