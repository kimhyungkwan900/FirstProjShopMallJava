package com.example.shop_mall_back.common.Exception;

public class InvalidPasswordException extends RuntimeException {
  public InvalidPasswordException() {
    super("ID 혹은 비밀번호가 일치하지 않습니다.");
  }
}