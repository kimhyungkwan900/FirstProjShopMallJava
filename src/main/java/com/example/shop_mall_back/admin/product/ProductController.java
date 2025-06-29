package com.example.shop_mall_back.admin.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/admin")
public class ProductController {
    private final ProductService productService;

    //상품 조회
    //상품 등록
    //상품 수정
    //상품 삭제
}
