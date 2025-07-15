package com.example.shop_mall_back.common.Exception;

public class InactiveAccountException extends RuntimeException {
    public InactiveAccountException() {
        super("비활성화된 계정입니다.");
    }
}
