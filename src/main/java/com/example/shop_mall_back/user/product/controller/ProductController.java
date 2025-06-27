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

    @GetMapping
    public Page<ProductDto> listProducts(Pageable pageable) {
        return productService.getProducts(pageable);
    }

    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable Long id) {
        ProductDto dto = productService.getProductById(id);
        List<ProductImageDto> images = productService.getProductImages(id);
        dto.setImages(images);
        return dto;
    }

    @GetMapping("/search")
    public Page<ProductDto> searchProducts(@RequestParam String keyword, Pageable pageable) {
        return productService.searchProducts(keyword, pageable);
    }

    @GetMapping("/filter")
    public Page<ProductDto> filterProducts(
            @RequestParam Optional<Long> categoryId,
            @RequestParam Optional<Long> brandId,
            @RequestParam Optional<Integer> minPrice,
            @RequestParam Optional<Integer> maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sortObj = Sort.by(Sort.Direction.fromString(direction), sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return productService.filterProducts(categoryId, brandId, minPrice, maxPrice, pageable);
    }

    @GetMapping("/popular")
    public Page<ProductDto> getPopularProducts(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "viewCount"));
        return productService.getProducts(pageable);
    }

    @GetMapping("/recent")
    public Page<ProductDto> getRecentProducts(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return productService.getProducts(pageable);
    }

    @GetMapping("/recommend")
    public Page<ProductDto> getRecommendedProducts(@RequestParam Long productId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.getRecommendedProducts(productId, pageable);
    }//브랜드

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
}
