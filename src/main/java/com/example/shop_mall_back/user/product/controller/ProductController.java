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

@RestController // 이 클래스가 REST API의 컨트롤러임을 나타냄. JSON 형태로 응답을 반환함.
@RequestMapping("/api/products") // 이 컨트롤러의 기본 URL 경로를 설정 (예: /api/products/...)
@RequiredArgsConstructor // final이 붙은 필드를 자동 생성자 주입으로 설정해주는 Lombok 어노테이션
public class ProductController {

    private final ProductService productService; // 상품 관련 비즈니스 로직을 처리하는 서비스 계층 주입

    // 상품 목록 조회 - 페이징과 정렬 처리 포함
    @GetMapping
    public Page<ProductDto> listProducts(
            @RequestParam(defaultValue = "0") int page, // page 번호 (기본값 0)
            @RequestParam(defaultValue = "10") int size, // 페이지당 항목 수 (기본값 10)
            @RequestParam(defaultValue = "id") String sort, // 정렬 기준 필드 (기본값 id)
            @RequestParam(defaultValue = "desc") String direction // 정렬 방향 (기본값 내림차순)
    ) {
        // 정렬 객체 생성 (오름차순 또는 내림차순)
        Sort sorting = direction.equalsIgnoreCase("asc")
                ? Sort.by(sort).ascending()
                : Sort.by(sort).descending();

        // 페이지 요청 객체 생성
        Pageable pageable = PageRequest.of(page, size, sorting);

        // 서비스 계층을 통해 페이징된 상품 목록을 가져와 반환
        return productService.getProducts(pageable);
    }

    // 특정 상품 상세 조회 + 이미지 포함
    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable Long id) {
        ProductDto dto = productService.getProductById(id); // ID 기준 상품 조회
        List<ProductImageDto> images = productService.getProductImages(id); // 해당 상품의 이미지 리스트 조회
        dto.setImages(images); // 조회된 이미지 리스트를 DTO에 설정
        return dto; // 최종 DTO 반환
    }

    // 키워드 기반 상품 검색 (페이징 포함)
    @GetMapping("/search")
    public Page<ProductDto> searchProducts(@RequestParam String keyword, Pageable pageable) {
        return productService.searchProducts(keyword, pageable); // 서비스에서 검색 결과 반환
    }

    // 필터 조건 기반 상품 검색
    @GetMapping("/filter")
    public Page<ProductDto> filterProducts(
            @RequestParam Optional<Long> categoryId,     // 카테고리 ID (선택적)
            @RequestParam Optional<Long> brandId,        // 브랜드 ID (선택적)
            @RequestParam Optional<Integer> minPrice,    // 최소 가격 (선택적)
            @RequestParam Optional<Integer> maxPrice,    // 최대 가격 (선택적)
            @RequestParam Optional<String> keyword,      // 키워드 (선택적)
            @RequestParam Optional<Boolean> includeChildren, // 하위 카테고리 포함 여부 (선택적)
            @RequestParam(defaultValue = "0") int page,  // 페이지 번호 (기본값 0)
            @RequestParam(defaultValue = "10") int size, // 페이지 크기 (기본값 10)
            @RequestParam(defaultValue = "id") String sort, // 정렬 기준 (기본값 id)
            @RequestParam(defaultValue = "desc") String direction // 정렬 방향 (기본값 desc)
    ) {
        // 정렬 객체 생성
        Sort sortObj = Sort.by(Sort.Direction.fromString(direction), sort);

        // 페이징 객체 생성
        Pageable pageable = PageRequest.of(page, size, sortObj);

        // 필터 조건들을 기반으로 상품 검색 실행
        return productService.filterProducts(categoryId, brandId, minPrice, maxPrice, keyword, includeChildren, pageable);
    }

    // 인기 상품 목록 조회 (조회수 기준 내림차순 정렬)
    @GetMapping("/popular")
    public Page<ProductDto> getPopularProducts(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        // 조회수(viewCount) 기준 내림차순 정렬로 페이징 객체 생성
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "viewCount"));

        // 인기 상품 조회 (내부적으로 getProducts(pageable)에서 정렬 기준만 변경해서 재활용)
        return productService.getProducts(pageable);
    }

    // 최신 상품 목록 조회 (ID 기준 내림차순: 최근 등록된 순서)
    @GetMapping("/recent")
    public Page<ProductDto> getRecentProducts(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        // ID 기준 내림차순 정렬로 최신 상품 조회
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return productService.getProducts(pageable);
    }

    // 추천 상품 조회 (같은 브랜드의 다른 상품들을 기준으로)
    @GetMapping("/recommend")
    public Page<ProductDto> getRecommendedProducts(@RequestParam Long productId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        // 단순 페이징 객체 생성
        Pageable pageable = PageRequest.of(page, size);

        // 추천 상품 조회 (같은 브랜드의 다른 상품들 등)
        return productService.getRecommendedProducts(productId, pageable);
    }

    // 특정 카테고리에 속한 상품 조회
    @GetMapping("/category/{categoryId}")
    public Page<ProductDto> getProductsByCategory(@PathVariable Long categoryId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sort,
                                                  @RequestParam(defaultValue = "desc") String direction) {
        // 정렬 객체 생성
        Sort sortObj = Sort.by(Sort.Direction.fromString(direction), sort);

        // 페이징 객체 생성
        Pageable pageable = PageRequest.of(page, size, sortObj);

        // 카테고리 ID에 해당하는 상품 목록 반환
        return productService.getProductsByCategory(categoryId, pageable);
    }

    // 특정 브랜드에 속한 상품 조회
    @GetMapping("/brand/{brandId}")
    public Page<ProductDto> getProductsByBrand(@PathVariable Long brandId,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(defaultValue = "id") String sort,
                                               @RequestParam(defaultValue = "desc") String direction) {
        // 정렬 객체 생성
        Sort sortObj = Sort.by(Sort.Direction.fromString(direction), sort);

        // 페이징 객체 생성
        Pageable pageable = PageRequest.of(page, size, sortObj);

        // 브랜드 ID에 해당하는 상품 목록 반환
        return productService.getProductsByBrand(brandId, pageable);
    }

    // 하위 카테고리를 포함한 전체 상품 조회 (계층 구조 활용)
    @GetMapping("/category/{categoryId}/all")
    public Page<ProductDto> getProductsByCategoryAndChildren(@PathVariable Long categoryId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(defaultValue = "id") String sort,
                                                             @RequestParam(defaultValue = "desc") String direction) {
        // 정렬 객체 생성
        Sort sortObj = Sort.by(Sort.Direction.fromString(direction), sort);

        // 페이징 객체 생성
        Pageable pageable = PageRequest.of(page, size, sortObj);

        // 특정 카테고리 + 하위 카테고리 포함된 상품 목록 반환
        return productService.getProductsByCategoryAndChildren(categoryId, pageable);
    }
}
