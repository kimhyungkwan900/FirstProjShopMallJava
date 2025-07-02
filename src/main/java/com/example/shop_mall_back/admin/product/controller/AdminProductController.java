package com.example.shop_mall_back.admin.product.controller;

import com.example.shop_mall_back.admin.product.dto.ProductFormDto;
import com.example.shop_mall_back.admin.product.service.AdminProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/admin")
public class AdminProductController {
    private final AdminProductService productService;

    //상품 목록 조회


    //상품 상세 조회


    //상품 등록
    @PostMapping("/products/new")
    public ResponseEntity<?> newProduct(@Valid @RequestBody ProductFormDto productFormDto, BindingResult bindingResult, Model model, @RequestParam("productImgFile") List<MultipartFile> productImgFileList) {

        if(bindingResult.hasErrors()){
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            FieldError::getDefaultMessage
                    ));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        if(productImgFileList.get(0).isEmpty() && productFormDto.getId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "첫번째 상품 이미지는 필수 입력 값 입니다."));
        }

        try{
            URI location = new URI("/api/admin/products/new");
            return ResponseEntity.created(location)
                    .body(Map.of("id", productService.saveProduct(productFormDto, productImgFileList)));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //상품 수정


    //상품 삭제


}
