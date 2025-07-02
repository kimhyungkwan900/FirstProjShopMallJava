package com.example.shop_mall_back.user.Cart.service;

import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    // 상품 정보를 조회하기 위한 JPA 리포지토리
    private final ProductRepository productRepository;


    @Override
    public boolean isSoldOut(Long productId) {
        // 주어진 productId로 상품을 조회, 없으면 예외 발생
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

        // 상품의 재고가 0 이하인 경우 품절로 판단
        return product.getStock() <= 0 ||
                "품절".equals(product.getSellStatus());
    }

    @Override
    public boolean isStockEnough(Long productId, int requestQuantity) {
        // 수량이 1개 이상인지 확인 (0이하일 경우 예외 발생)
        if (requestQuantity <= 0) {
            throw new IllegalArgumentException("수량은 1개 이상이어야 합니다.");
        }

        // 상품이 존재하는지 확인, 없으면 예외 발생
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

        // 요청 수량보다 재고가 많거나 같으면 true (주문 가능), 아니면 false
        return product.getStock() >= requestQuantity;
    }
}
