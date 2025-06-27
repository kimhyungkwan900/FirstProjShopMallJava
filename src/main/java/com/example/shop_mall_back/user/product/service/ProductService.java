package com.example.shop_mall_back.user.product.service;

import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.product.dto.ProductDto;
import com.example.shop_mall_back.user.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductDto> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductDto::from);
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);
        return ProductDto.from(product);
    }

    public Page<ProductDto> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByNameContaining(keyword, pageable)
                .map(ProductDto::from);
    }

    public Page<ProductDto> filterProducts(Optional<Long> categoryId,
                                           Optional<Long> brandId,
                                           Optional<Integer> minPrice,
                                           Optional<Integer> maxPrice,
                                           Pageable pageable) {
        return productRepository.findAll((root, query, cb) -> {
            var predicates = cb.conjunction();
            categoryId.ifPresent(cid -> predicates.getExpressions().add(
                    cb.equal(root.get("category").get("id"), cid)));
            brandId.ifPresent(bid -> predicates.getExpressions().add(
                    cb.equal(root.get("brand").get("id"), bid)));
            minPrice.ifPresent(min -> predicates.getExpressions().add(
                    cb.greaterThanOrEqualTo(root.get("price"), min)));
            maxPrice.ifPresent(max -> predicates.getExpressions().add(
                    cb.lessThanOrEqualTo(root.get("price"), max)));
            return predicates;
        }, pageable).map(ProductDto::from);
    }

    public Page<ProductDto> getRecommendedProducts(Long productId, Pageable pageable) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        Long brandId = product.getBrand().getId();
        return productRepository.findByBrandId(brandId, pageable)
                .map(ProductDto::from);
    }//브랜드



    public Page<ProductDto> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable)
                .map(ProductDto::from);
    }

    public Page<ProductDto> getProductsByBrand(Long brandId, Pageable pageable) {
        return productRepository.findByBrandId(brandId, pageable)
                .map(ProductDto::from);
    }
}
