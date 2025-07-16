package com.example.shop_mall_back.admin.product.controller;

import com.example.shop_mall_back.admin.product.dto.*;
import com.example.shop_mall_back.admin.product.service.AdminProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminProductController {
    private final AdminProductService adminproductService;
    private final AdminProductService adminProductService;

    //---상품 등록
    @PostMapping("/products/new")
    public ResponseEntity<?> newProduct(@Valid @ModelAttribute ProductFormDto productFormDto, BindingResult bindingResult,
                                        @RequestParam("productImgFile") List<MultipartFile> productImgFileList) {

        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("상품 등록 실패");
        }

        if(productImgFileList.get(0).isEmpty() && productFormDto.getId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "첫번째 상품 이미지는 필수 입력 값 입니다."));
        }

        try{
            adminproductService.saveProduct(productFormDto, productImgFileList);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 등록 중 에러 발생");
        }
    }

    //---조회 조건과 페이지 정보를 받아서 상품 데이터 조회
    @GetMapping({"/products", "/products/{page}"})
    public ResponseEntity<?> productManage(@ModelAttribute ProductSearchDto productSearchDto, @RequestParam(value="page", defaultValue = "0") int page){

        Pageable pageable = PageRequest.of(page,8);
        Page<ProductDto> products = adminProductService.getAdminProductPage(productSearchDto, pageable);

        ProductListDto productListDto = ProductListDto.builder()
                .products(products)
                .productSearchDto(productSearchDto)
                .maxPage(10)
                .totalPage(products.getTotalPages())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(productListDto);
    }

    //---상품 수정
    @PutMapping("/products/update")
    public ResponseEntity<?> updateProudct(@Valid @ModelAttribute ProductFormDto productFormDto, BindingResult bindingResult,
                                           @RequestParam("productImgFile") List<MultipartFile> productImgFileList){

        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("상품 수정 실패");
        }

        if(productImgFileList.get(0).isEmpty() && productFormDto.getId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("첫번째 상품 이미지는 필수 입력 값 입니다.");
        }

        try {
            adminProductService.updateProduct(productFormDto, productImgFileList);
            return ResponseEntity.ok().build();
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("입력값 오류");
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 처리 중 오류가 발생했습니다.");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 수정 중 서버 오류가 발생하였습니다.");
        }
    }

    //---상품 삭제
    @DeleteMapping("/products")
    public ResponseEntity<?> deleteProducts(@RequestBody List<Long> productIds){
        try {
            adminProductService.deleteProducts(productIds);
            return ResponseEntity.ok("상품이 삭제되었습니다.");
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 상품을 찾을 수 없습니다.");
        }
        catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("상품 삭제 권한이 없습니다.");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 삭제 중 오류가 발생했습니다.");
        }
    }
}
