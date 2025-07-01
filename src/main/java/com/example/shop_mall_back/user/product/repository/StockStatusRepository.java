//package com.example.shop_mall_back.user.product.repository;
//
//import com.example.shop_mall_back.common.domain.Product;
//import com.example.shop_mall_back.user.product.domain.StockStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public interface StockStatusRepository extends JpaRepository<StockStatus, Long> {
//    /**
//     * 특정 상품에 대한 재고 상태 정보를 조회
//     * - 상품 상세 페이지 또는 관리자 화면에서 재고 여부를 확인할 때 사용
//     * - 예: SELECT * FROM stock_status WHERE product_id = ?
//     * - 결과가 없을 수도 있으므로 Optional로 감싸 반환
//     */
//    Optional<StockStatus> findByProduct(Product product);
//}
