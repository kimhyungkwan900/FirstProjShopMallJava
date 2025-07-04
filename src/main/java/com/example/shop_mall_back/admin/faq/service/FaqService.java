package com.example.shop_mall_back.admin.faq.service;

import com.example.shop_mall_back.admin.faq.domain.Faq;
import com.example.shop_mall_back.admin.faq.dto.FaqDto;
import com.example.shop_mall_back.admin.faq.dto.FaqSearchDto;
import com.example.shop_mall_back.admin.faq.repository.FaqRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class FaqService {

    private final FaqRepository faqRepository;


    //1. FAQ 등록


    // 2. 전체 목록 페이징


    //3. 검색(카테고리는 필수로, 키워드는 옵션임)


    // 4. 상세 조회


    // 5. 수정


    // 6. 삭제


}
