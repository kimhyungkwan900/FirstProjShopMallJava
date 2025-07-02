package com.example.shop_mall_back.common.service.serviceimpl;

import com.example.shop_mall_back.common.constant.LoginResult;
import com.example.shop_mall_back.common.constant.LoginType;
import com.example.shop_mall_back.common.domain.login.LoginHistory;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.repository.LoginHistoryRepository;
import com.example.shop_mall_back.common.service.serviceinterface.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginHistoryServiceImpl implements LoginHistoryService {

    private final LoginHistoryRepository loginHistoryRepository;

    @Override
    public void recordLogin(Member member, String ip, String userAgent, LoginResult result, LoginType type) {
        // 로그인 history 생성
        LoginHistory history = LoginHistory.builder()
                .member(member)
                .ipAddress(ip)
                .userAgent(userAgent)
                .loginResult(result)
                .loginType(type)
                .build();
        
        loginHistoryRepository.save(history);
    }

    @Override
    public void recordSuccessLogin(Member member, String ip, String userAgent, LoginType type) {

    }

    @Override
    public void recordFailLogin(String userId, String ip, String userAgent, LoginType type) {

    }

    @Override
    public void recordLogout(Member member) {

    }
}
