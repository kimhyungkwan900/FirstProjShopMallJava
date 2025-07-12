package com.example.shop_mall_back.user.myOrder.service;

import com.example.shop_mall_back.admin.order.domain.ClaimManage;
import com.example.shop_mall_back.admin.order.repository.ClaimManageRepository;
import com.example.shop_mall_back.common.domain.Order;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.Order.domain.OrderItem;
import com.example.shop_mall_back.user.myOrder.domain.OrderDelete;
import com.example.shop_mall_back.user.myOrder.domain.OrderReturn;
import com.example.shop_mall_back.user.myOrder.dto.OrderListDTO;
import com.example.shop_mall_back.user.myOrder.dto.OrderProductDTO;
import com.example.shop_mall_back.user.myOrder.dto.OrderReturnDTO;
import com.example.shop_mall_back.user.myOrder.repository.*;
import com.example.shop_mall_back.user.product.dto.ProductImageDto;
import com.example.shop_mall_back.user.product.service.ProductService;
import com.example.shop_mall_back.user.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MyOrderService {

    private final MyOrderRepository myOrderRepository;
    private final MyOrderItemRepository myOrderItemRepository;
    private final ProductService productService;
    private final MyOrderManageRepository myOrderManageRepository;
    private final OrderReturnRepository orderReturnRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final MyOrderDeleteRepository myOrderDeleteRepository;
    private final ClaimManageRepository claimManageRepository;

    // 회원 별 주문 목록 (네이티브 쿼리로 필터)
    public Page<OrderListDTO> findByMemberIdAndFilterNative(Long memberId,
                                                            String keyword,
                                                            LocalDateTime startDate,
                                                            LocalDateTime endDate,
                                                            Pageable pageable) {
        Page<Order> ordersPage = myOrderRepository.findOrdersByFilterNative(memberId, keyword, startDate, endDate, pageable);

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

            dto.setOrderDelete(checkOrderStatus(order.getId()));

            List<OrderItem> orderItems = myOrderItemRepository.findByOrderId(order.getId());
            if (!orderItems.isEmpty()) {
                Product product = orderItems.get(0).getProduct();
                dto.setProduct(toOrderProductDTO(product));
            }
            return dto;
        });
    }


    // 회원 별 주문 목록 조회
//    public Page<OrderListDTO> findByMemberId(Long memberId, Pageable pageable) {
//        Page<Order> ordersPage = myOrderRepository.findByMemberId(memberId, pageable);
//        System.out.println("ordersPage = " + ordersPage);
//
//        return ordersPage.map(order -> {
//            OrderListDTO dto = new OrderListDTO();
//            dto.setId(order.getId());
//            dto.setOrderId(order.getId());
//            dto.setMemberId(order.getMember().getId());
//            dto.setOrderDate(order.getOrderDate());
//            dto.setTotalAmount(order.getTotalAmount());
//            dto.setTotalCount(order.getTotalCount());
//            dto.setPaymentMethod(order.getPaymentMethod());
//            dto.setOrderStatus(myOrderManageRepository.findOrderStatusByOrderId(order.getId()));
//            dto.setReturnType(orderReturnRepository.getReturnTypeByOrderId(order.getId()));
//            dto.setExistsReview(reviewRepository.existsByOrderId(order.getId()));
//            List<OrderItem> orderItems = myOrderItemRepository.findByOrderId(order.getId());
//            if (!orderItems.isEmpty()) {
//                Product product = orderItems.get(0).getProduct();
//                dto.setProduct(toOrderProductDTO(product));
//            }
//
//            return dto;
//        });
//    }


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

    // 반품 및 취소 신청
    public void insertOrderReturn(OrderReturnDTO orderReturnDTO) {
        OrderReturn orderReturn = modelMapper.map(orderReturnDTO, OrderReturn.class);
        orderReturnRepository.save(orderReturn);

        //반품/취소 관리 엔티티 생성
        ClaimManage claimManage = new ClaimManage();
        claimManage.setOrderReturn(orderReturn);
        claimManageRepository.save(claimManage);
    }

    // 회원 주문 목록 삭제
    public void deleteOrder(Long orderId) {
        OrderDelete orderDelete = new OrderDelete();
        orderDelete.setOrderId(orderId);
        myOrderDeleteRepository.save(orderDelete);
    }

    // 회원 목록이 삭제인지 아닌지 확인
    private boolean checkOrderStatus(Long orderId) {
        return myOrderDeleteRepository.existsByOrderId(orderId);
    }
}
