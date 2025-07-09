package com.example.shop_mall_back.user.myOrder.service;

import com.example.shop_mall_back.common.domain.Order;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.Order.domain.OrderItem;
import com.example.shop_mall_back.user.myOrder.dto.OrderListDTO;
import com.example.shop_mall_back.user.myOrder.dto.OrderProductDTO;
import com.example.shop_mall_back.user.myOrder.repository.MyOrderItemRepository;
import com.example.shop_mall_back.user.myOrder.repository.MyOrderManageRepository;
import com.example.shop_mall_back.user.myOrder.repository.MyOrderRepository;
import com.example.shop_mall_back.user.myOrder.repository.OrderReturnRepository;
import com.example.shop_mall_back.user.product.dto.ProductImageDto;
import com.example.shop_mall_back.user.product.service.ProductService;
import com.example.shop_mall_back.user.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyOrderService {

    private final MyOrderRepository myOrderRepository;
    private final MyOrderItemRepository myOrderItemRepository;
    private final ProductService productService;
    private final MyOrderManageRepository myOrderManageRepository;
    private final OrderReturnRepository orderReturnRepository;
    private final ReviewRepository reviewRepository;

    // 회원 별 주문 목록 조회
    public Page<OrderListDTO> findByMemberId(Long memberId, Pageable pageable) {
        Page<Order> ordersPage = myOrderRepository.findByMemberId(memberId, pageable);
        System.out.println("ordersPage = " + ordersPage);

        return ordersPage.map(order -> {
            OrderListDTO dto = new OrderListDTO();

            dto.setId(order.getId());
            dto.setOrderId(order.getId());
            dto.setMemberId(order.getMember().getId());
            dto.setOrderDate(order.getOrderDate());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setTotalCount(order.getTotalCount());
            dto.setPaymentMethod(order.getPaymentMethod());
            dto.setOrderStatus(myOrderManageRepository.findOrderStatusByOrderId(order.getId()));

            dto.setReturnType(orderReturnRepository.getReturnTypeByOrderId(order.getId()));

            dto.setExistsReview(reviewRepository.existsByOrderId(order.getId()));

            List<OrderItem> orderItems = myOrderItemRepository.findByOrderId(order.getId());
            if (!orderItems.isEmpty()) {
                Product product = orderItems.get(0).getProduct();
                dto.setProduct(toOrderProductDTO(product));
            }

            return dto;
        });
    }

    public OrderProductDTO toOrderProductDTO(Product product) {
        List<ProductImageDto> images = productService.getProductImages(product.getId());

        OrderProductDTO dto = new OrderProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        if (images != null && !images.isEmpty()) {
            dto.setImage(images.get(0));
        } else {
            dto.setImage(null); // 없으면 null 처리
        }
        return dto;
    }
}
