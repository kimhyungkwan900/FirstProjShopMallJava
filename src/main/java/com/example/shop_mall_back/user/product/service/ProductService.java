package com.example.shop_mall_back.user.product.service;

import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.product.domain.ProductImage;
import com.example.shop_mall_back.user.product.dto.ProductDto;
import com.example.shop_mall_back.user.product.dto.ProductImageDto;
import com.example.shop_mall_back.user.product.repository.ProductImageRepository;
import com.example.shop_mall_back.user.product.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    // 상품 관련 DB 접근을 위한 리포지토리
    private final ProductRepository productRepository;

    // 상품 이미지 관련 DB 접근을 위한 리포지토리
    private final ProductImageRepository productImageRepository;

    // 카테고리 서비스 (자식 카테고리 ID 조회용)
    private final CategoryService categoryService;

    /**
     * 전체 상품을 페이징 형태로 조회
     * - 정렬, 페이지 크기, 번호 등은 Pageable 객체로 조정
     */
    public Page<ProductDto> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(p -> {
                    ProductDto dto = ProductDto.from(p);
                    dto.setImages(getProductImages(p.getId())); // ⭐
                    return dto;
                });
    }

    /**
     * 특정 상품의 상세 정보 조회 + 조회수 증가
     * - 조회수(viewCount)를 1 증가시키고 저장 후 반환
     */
    @Transactional
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);
        return ProductDto.from(product);
    }

    /**
     * 키워드 기반 상품 이름 검색 (like 검색)
     * - 사용자가 입력한 단어가 포함된 상품명 검색
     */
    public Page<ProductDto> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByNameContaining(keyword, pageable)
                .map(p -> {
                    ProductDto dto = ProductDto.from(p);
                    dto.setImages(getProductImages(p.getId()));
                    return dto;
                });
    }

    /**
     * 다양한 필터 조건 (카테고리, 브랜드, 가격 범위)로 상품 검색
     * - categoryId, brandId, minPrice, maxPrice는 Optional로 받아 유동적으로 조건 구성
     * - CriteriaBuilder를 통해 조건 동적 생성
     */
    public Page<ProductDto> filterProducts(Optional<Long> categoryId,
                                           Optional<Long> brandId,
                                           Optional<Integer> minPrice,
                                           Optional<Integer> maxPrice,
                                           Optional<String> keyword,
                                           Optional<Boolean> includeChildren,
                                           Pageable pageable) {
        System.out.println("✅ filterProducts called with keyword: " + keyword);
        return productRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // ✅ 카테고리: includeChildren이 true면 하위 포함
            if (categoryId.isPresent()) {
                if (includeChildren.orElse(false)) {
                    List<Long> categoryIds = categoryService.getAllChildCategoryIds(categoryId.get());
                    predicates.add(root.get("category").get("id").in(categoryIds));
                } else {
                    predicates.add(cb.equal(root.get("category").get("id"), categoryId.get()));
                }
            }

            brandId.ifPresent(bid -> predicates.add(
                    cb.equal(root.get("brand").get("id"), bid)));

            minPrice.ifPresent(min -> predicates.add(
                    cb.greaterThanOrEqualTo(root.get("price"), min)));

            maxPrice.ifPresent(max -> predicates.add(
                    cb.lessThanOrEqualTo(root.get("price"), max)));

            keyword.filter(kw -> !kw.trim().isEmpty())
                    .ifPresent(kw -> {
                        System.out.println("🔍 keyword filter: " + kw);
                        predicates.add(
                                cb.like(cb.lower(root.get("name").as(String.class)), "%" + kw.toLowerCase() + "%")
                        );
                    });

            // 🔽 핵심: query.where(...)에 명시적으로 적용
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable).map(ProductDto::from);

    }

    /**
     * 추천 상품 조회 (같은 브랜드의 다른 상품들)
     * - 현재 상품과 같은 브랜드의 상품 목록을 반환
     */
    public Page<ProductDto> getRecommendedProducts(Long productId, Pageable pageable) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        Long brandId = product.getBrand().getId();

        return productRepository.findByBrandId(brandId, pageable)
                .map(p -> {
                    ProductDto dto = ProductDto.from(p);
                    List<ProductImageDto> images = getProductImages(p.getId());
                    dto.setImages(getProductImages(p.getId()));
                    return dto;
                });
    }


    /**
     * 특정 카테고리에 속한 상품 조회
     */
    public Page<ProductDto> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable)
                .map(p -> {
                    ProductDto dto = ProductDto.from(p);
                    dto.setImages(getProductImages(p.getId())); // ⭐
                    return dto;
                });
    }

    /**
     * 특정 브랜드에 속한 상품 조회
     */
    public Page<ProductDto> getProductsByBrand(Long brandId, Pageable pageable) {
        return productRepository.findByBrandId(brandId, pageable)
                .map(p -> {
                    ProductDto dto = ProductDto.from(p);
                    dto.setImages(getProductImages(p.getId())); // ⭐
                    return dto;
                });
    }

    /**
     * 특정 상품의 이미지 목록을 조회
     * - 등록 순서대로 반환 (대표 이미지, 서브 이미지 포함)
     */
    public List<ProductImageDto> getProductImages(Long productId) {
        List<ProductImage> images = productImageRepository.findByProductIdOrderByIdAsc(productId);
        return images.stream().map(ProductImageDto::from).toList();
    }

    /**
     * 특정 카테고리 및 모든 하위 카테고리에 속한 상품들 조회
     * - 트리 구조의 카테고리까지 모두 포함해서 상품 검색
     */
    public Page<ProductDto> getProductsByCategoryAndChildren(Long categoryId, Pageable pageable) {
        List<Long> categoryIds = categoryService.getAllChildCategoryIds(categoryId); // 재귀 포함
        return productRepository.findByCategoryIdIn(categoryIds, pageable)
                .map(p -> {
                    ProductDto dto = ProductDto.from(p);
                    dto.setImages(getProductImages(p.getId())); // ⭐
                    return dto;
                });
    }
}
