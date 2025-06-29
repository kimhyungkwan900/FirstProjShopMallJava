package com.example.shop_mall_back.common.service;

import com.example.shop_mall_back.common.domain.Member;
import com.example.shop_mall_back.common.dto.MemberFormDTO;
import com.example.shop_mall_back.common.repository.MemberProfileRepository;
import com.example.shop_mall_back.common.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberProfileRepository memberProfileRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입")
    void signUpTest(){
        MemberFormDTO memberFormDTO = MemberFormDTO.builder()
                .user_id("testuser")
                .user_password("123456")
                .email("testuser@example.com")
                .phone_number("01012345678")
                .build();

        // when
        Member dumMember = Member.create(memberFormDTO,passwordEncoder);

        Long savedId = memberService.signUp(memberFormDTO,passwordEncoder);

        assertNotNull(savedId);

        System.out.println("====================");
        System.out.println(dumMember);
    }

}