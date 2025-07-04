package com.example.shop_mall_back.user.Cart.service;

import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Mockito 확장 기능으로 테스트 실행
class InventoryServiceImplTest {

    @InjectMocks
    private InventoryServiceImpl inventoryService; // 테스트 대상 서비스

    @Mock
    private ProductRepository productRepository;   // ProductRepository Mock 객체

    @Mock
    private RestockAlarmService restockAlarmService; // RestockAlarmService Mock 객체

    /**
     * 재고가 0인 경우 품절로 처리되는지 검증
     */
    @Test
    @DisplayName("재고 0이면 품절 처리 테스트 : 재고 0")
    public void isSoldOut_success(){
        // given: 재고가 0인 상품 준비
        Long productId = 1L;
        Product product = Product.builder()
                .id(productId)
                .stock(0) // 재고 0
                .sellStatus(Product.SellStatus.판매중)
                .build();

        // Mock: productRepository가 해당 상품 반환하도록 설정
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // when: 재고 확인 메소드 호출
        boolean result = inventoryService.isSoldOut(productId);

        // then: 결과가 true인지 검증
        assertTrue(result);
    }

    /**
     * 존재하지 않는 상품 ID로 호출 시 예외 발생 여부 검증
     */
    @Test
    @DisplayName("재고 0이면 품절 처리 테스트 : 예외 - 상품 없음")
    void isSoldOut_noProduct_exception(){
        // given: 존재하지 않는 상품 ID
        Long productId = 6L;

        // Mock: productRepository가 빈 Optional 반환
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // when + then: 예외 검증
        assertThatThrownBy(() -> inventoryService.isSoldOut(productId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 상품이 존재하지 않습니다.");
    }

    /**
     * 요청 수량이 재고보다 적거나 같은 경우 true 반환 검증
     */
    @Test
    @DisplayName("요청 수량 < 재고 → true : 성공")
    void isStockEnough_success(){
        // given: 재고가 요청 수량보다 많은 상품
        Long productId = 1L;
        int requestQuantity = 10;
        Product product = Product.builder()
                .id(productId)
                .stock(20) // 재고 20
                .sellStatus(Product.SellStatus.판매중)
                .build();

        // Mock: productRepository가 해당 상품 반환
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // when: 재고 충분 여부 확인
        boolean result = inventoryService.isStockEnough(productId, requestQuantity);

        // then: true 반환 검증
        assertTrue(result);
    }

    /**
     * 요청 수량이 재고보다 많은 경우 false 반환 검증
     */
    @Test
    @DisplayName("요청 수량 > 재고 → false : 예외")
    void isStockEnough_insufficientStock_returnsFalse(){
        // given: 재고가 요청 수량보다 적은 상품
        Long productId = 4L;
        int requestQuantity = 15;
        Product product = Product.builder()
                .id(productId)
                .stock(10) // 재고 10
                .sellStatus(Product.SellStatus.판매중)
                .build();

        // Mock: productRepository가 해당 상품 반환
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // when: 재고 충분 여부 확인
        boolean result = inventoryService.isStockEnough(productId, requestQuantity);

        // then: false 반환 검증
        assertFalse(result);
    }

    /**
     * 요청 수량이 0 이하일 때 예외 발생 여부 검증
     */
    @Test
    @DisplayName("요청 수량 <= 0 : 예외")
    void isStockEnough_invalidQuantity_throwsException() {
        // given: 요청 수량이 0인 경우
        Long productId = 5L;
        int invalidQuantity = 0;

        // when + then: 예외 검증
        assertThatThrownBy(() -> inventoryService.isStockEnough(productId, invalidQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량은 1개 이상이어야 합니다.");
    }

    /**
     * 존재하지 않는 상품 ID로 호출 시 예외 발생 여부 검증
     */
    @Test
    @DisplayName("상품 없음 : 예외")
    void isStockEnough_noProduct_exception() {
        // given: 존재하지 않는 상품 ID
        Long productId = 7L;
        int requestQuantity = 5;

        // Mock: productRepository가 빈 Optional 반환
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // when + then: 예외 검증
        assertThatThrownBy(() -> inventoryService.isStockEnough(productId, requestQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 상품이 존재하지 않습니다.");
    }
}
