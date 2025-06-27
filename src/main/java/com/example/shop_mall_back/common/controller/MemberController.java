package com.example.shop_mall_back.common.controller;


import com.example.shop_mall_back.common.dto.MemberFormDTO;
import com.example.shop_mall_back.common.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {
    private final MemberService memberService;



}
