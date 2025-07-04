package com.example.shop_mall_back.user.product.controller;

import com.example.shop_mall_back.user.product.dto.ProductDto;
import com.example.shop_mall_back.user.product.dto.ProductImageDto;
import com.example.shop_mall_back.user.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 모든 상품을 페이징 처리하여 반환
    // 모든 상품을 페이징 + 정렬 처리하여 반환
    @GetMapping
    public Page<ProductDto> listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sorting = direction.equalsIgnoreCase("asc")
                ? Sort.by(sort).ascending()
                : Sort.by(sort).descending();

        Pageable pageable = PageRequest.of(page, size, sorting);
        return productService.getProducts(pageable);
    }

    // 특정 상품 상세 조회 (이미지도 함께 조회)
    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable Long id) {
        ProductDto dto = productService.getProductById(id);
        List<ProductImageDto> images = productService.getProductImages(id);
        dto.setImages(images);
        return dto;
    }

    // 키워드 기반 상품 검색 기능
    @GetMapping("/search")
    public Page<ProductDto> searchProducts(@RequestParam String keyword, Pageable pageable) {
        return productService.searchProducts(keyword, pageable);
    }

    // 카테고리, 브랜드, 가격 범위, 정렬 기준 등 필터 조건으로 상품 검색
    @GetMapping("/filter")
    public Page<ProductDto> filterProducts(
            @RequestParam Optional<Long> categoryId,     // 선택적 카테고리 ID
            @RequestParam Optional<Long> brandId,        // 선택적 브랜드 ID
            @RequestParam Optional<Integer> minPrice,    // 선택적 최소 가격
            @RequestParam Optional<Integer> maxPrice,    // 선택적 최대 가격
            @RequestParam Optional<String> keyword,
            @RequestParam Optional<Boolean> includeChildren,
            @RequestParam(defaultValue = "0") int page,  // 기본 페이지 번호: 0
            @RequestParam(defaultValue = "10") int size, // 기본 페이지 크기: 10
            @RequestParam(defaultValue = "id") String sort,         // 기본 정렬 컬럼: id
            @RequestParam(defaultValue = "desc") String direction   // 정렬 방향: 내림차순
    ) {
        // 정렬 객체 생성
        Sort sortObj = Sort.by(Sort.Direction.fromString(direction), sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return productService.filterProducts(categoryId, brandId, minPrice, maxPrice, keyword, includeChildren, pageable);
    }

    // 인기 상품 목록 조회 (조회수 기준 내림차순)
    @GetMapping("/popular")
    public Page<ProductDto> getPopularProducts(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "viewCount"));
        return productService.getProducts(pageable);
    }

    // 최신 상품 목록 조회 (등록순 기준 내림차순)
    @GetMapping("/recent")
    public Page<ProductDto> getRecentProducts(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return productService.getProducts(pageable);
    }

    // 특정 상품을 기준으로 같은 브랜드의 추천 상품 조회
    @GetMapping("/recommend")
    public Page<ProductDto> getRecommendedProducts(@RequestParam Long productId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.getRecommendedProducts(productId, pageable);
    }

    // 특정 카테고리에 속한 상품 조회
    @GetMapping("/category/{categoryId}")
    public Page<ProductDto> getProductsByCategory(@PathVariable Long categoryId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sort,
                                                  @RequestParam(defaultValue = "desc") String direction) {
        Sort sortObj = Sort.by(Sort.Direction.fromString(direction), sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return productService.getProductsByCategory(categoryId, pageable);
    }

    // 특정 브랜드에 속한 상품 조회
    @GetMapping("/brand/{brandId}")
    public Page<ProductDto> getProductsByBrand(@PathVariable Long brandId,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(defaultValue = "id") String sort,
                                               @RequestParam(defaultValue = "desc") String direction) {
        Sort sortObj = Sort.by(Sort.Direction.fromString(direction), sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return productService.getProductsByBrand(brandId, pageable);
    }

    // 하위 카테고리를 포함한 카테고리 전체 상품 조회
    @GetMapping("/category/{categoryId}/all")
    public Page<ProductDto> getProductsByCategoryAndChildren(@PathVariable Long categoryId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(defaultValue = "id") String sort,
                                                             @RequestParam(defaultValue = "desc") String direction) {
        Sort sortObj = Sort.by(Sort.Direction.fromString(direction), sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return productService.getProductsByCategoryAndChildren(categoryId, pageable);
    }
}
