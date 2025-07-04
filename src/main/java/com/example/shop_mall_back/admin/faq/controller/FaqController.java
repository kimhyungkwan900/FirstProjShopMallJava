package com.example.shop_mall_back.admin.faq.controller;

import com.example.shop_mall_back.admin.faq.dto.FaqDto;
import com.example.shop_mall_back.admin.faq.repository.FaqRepository;
import com.example.shop_mall_back.admin.faq.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/admin/faqs")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;


}
