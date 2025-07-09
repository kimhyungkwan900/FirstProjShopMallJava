package com.example.shop_mall_back.admin.product.controller;

import com.example.shop_mall_back.admin.product.dto.*;
import com.example.shop_mall_back.admin.product.repository.AdminProductRepository;
import com.example.shop_mall_back.admin.product.service.AdminProductService;
import com.example.shop_mall_back.common.domain.Product;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/admin")
public class AdminProductController {
    private final AdminProductService adminproductService;
    private final AdminProductRepository adminProductRepository;
    private final AdminProductService adminProductService;

    //---상품 등록
    @PostMapping("/products/new")
    public ResponseEntity<?> newProduct(@Valid @RequestBody ProductFormDto productFormDto, BindingResult bindingResult, Model model, @RequestParam("productImgFile") List<MultipartFile> productImgFileList) {

        if(bindingResult.hasErrors()){
//            Map<String, String> errors = bindingResult.getFieldErrors().stream()
//                    .collect(Collectors.toMap(
//                            FieldError::getField,
//                            FieldError::getDefaultMessage
//                    ));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("상품 등록 실패");
        }

        if(productImgFileList.get(0).isEmpty() && productFormDto.getId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "첫번째 상품 이미지는 필수 입력 값 입니다."));
        }

        try{
            URI location = new URI("/api/admin/products");

            return ResponseEntity.created(location).body(Map.of("id",
                    adminproductService.saveProduct(productFormDto, productImgFileList)));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 등록 중 에러 발생");
        }
    }

    //---조회 조건과 페이지 정보를 받아서 상품 데이터 조회
    @GetMapping({"/products", "/products/{page}"})
    public ResponseEntity<?> productManage(@ModelAttribute ProductSearchDto productSearchDto, @PathVariable("page") Optional<Integer> page){

        log.info(">>> incoming searchDto: {}", productSearchDto);

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<ProductDto> products = adminProductService.getAdminProductPage(productSearchDto, pageable);

        ProductListDto productListDto = ProductListDto.builder()
                .products(products)
                .productSearchDto(productSearchDto)
                .maxPage(10)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(productListDto);
    }

    //---상품 상세정보 조회
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDetailDto> productDetail(@PathVariable("productId") Long productId){

        ProductDetailDto productDetailDto = adminProductService.getProductDetail(productId);

        return ResponseEntity.status(HttpStatus.OK).body(productDetailDto);
    }

    //---상품 수정
    @PatchMapping("/products/{productId}")
    public ResponseEntity<?> updateProudct(@Valid @RequestBody ProductFormDto productFormDto, BindingResult bindingResult, @RequestParam("productImgFile") List<MultipartFile> productImgFileList){

        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("상품 수정 실패");
        }

        if(productImgFileList.get(0).isEmpty() && productFormDto.getId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("첫번째 상품 이미지는 필수 입력 값 입니다.");
        }

        try {
            URI location = new URI("/api/admin/products");

            return ResponseEntity.created(location).body(Map.of("id",
                    adminProductService.updateProduct(productFormDto, productImgFileList)));
        }
        catch (IllegalArgumentException e) {
            log.error("잘못된 입력값으로 인한 오류", e);
            return ResponseEntity.badRequest().body("입력값 오류");
        }
        catch (IOException e) {
            log.error("파일 처리 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 처리 중 오류가 발생했습니다.");
        }
        catch (Exception e) {
            log.error("상품 수정 중 예상치 못한 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 수정 중 서버 오류가 발생하였습니다.");
        }
    }

    //---상품 삭제
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") Long productId){
        try {
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
