package com.example.shop_mall_back.admin.faq.repository;

import com.example.shop_mall_back.admin.faq.domain.Faq;
import com.example.shop_mall_back.admin.faq.dto.FaqSearchDto;
import com.example.shop_mall_back.admin.faq.dto.PageRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FaqRepositoryCustom {
    Page<Faq> searchFaqs(FaqSearchDto  faqSearchDto, PageRequestDto pageRequestDto);
}
