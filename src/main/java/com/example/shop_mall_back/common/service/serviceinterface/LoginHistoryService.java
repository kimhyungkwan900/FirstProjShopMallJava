package com.example.shop_mall_back.common.service.serviceinterface;

import com.example.shop_mall_back.common.constant.LoginResult;
import com.example.shop_mall_back.common.constant.LoginType;
import com.example.shop_mall_back.common.domain.member.Member;

public interface LoginHistoryService {
    void recordSuccessLogin(Member member, String ip, String userAgent, LoginType type);
    void recordFailLogin(String userId, String ip, String userAgent, LoginType type);
    void recordLogout(Member member);
    void recordLogin(Member member, String ip, String userAgent, LoginResult result, LoginType type);
}
