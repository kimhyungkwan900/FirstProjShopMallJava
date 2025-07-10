package com.example.shop_mall_back.user.myOrder.service;

import com.example.shop_mall_back.common.domain.Order;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.Order.domain.OrderItem;
import com.example.shop_mall_back.user.myOrder.domain.OrderReturn;
import com.example.shop_mall_back.user.myOrder.dto.OrderChangeHistoryDTO;
import com.example.shop_mall_back.user.myOrder.dto.OrderProductDTO;
import com.example.shop_mall_back.user.myOrder.repository.MyOrderItemRepository;
import com.example.shop_mall_back.user.myOrder.repository.MyOrderRepository;
import com.example.shop_mall_back.user.myOrder.repository.OrderReturnRepository;
import com.example.shop_mall_back.user.product.dto.ProductImageDto;
import com.example.shop_mall_back.user.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class MyOrderChangeService {

    private final OrderReturnRepository orderReturnRepository;
    private final MyOrderRepository myOrderRepository; // orderId → Order 조회용
    private final MyOrderItemRepository myOrderItemRepository;
    private final ProductService productService;

    public Page<OrderChangeHistoryDTO> getOrderChangeHistory(Long memberId, OrderReturn.ReturnType returnType, Pageable pageable) {
        Page<OrderReturn> returnsPage = (returnType != null)
                ? orderReturnRepository.findByMemberIdAndReturnType(memberId, returnType, pageable)
                : orderReturnRepository.findByMemberId(memberId, pageable);

        return returnsPage.map(this::toDTO);
    }

    private OrderChangeHistoryDTO toDTO(OrderReturn orderReturn) {
        OrderChangeHistoryDTO dto = new OrderChangeHistoryDTO();
        dto.setId(orderReturn.getId());
        dto.setMemberId(orderReturn.getMemberId());
        dto.setOrderId(orderReturn.getOrderId());
        dto.setReturnType(orderReturn.getReturnType());
        dto.setReason(orderReturn.getReason());
        dto.setDetail(orderReturn.getDetail());
        dto.setRegDate(orderReturn.getRegDate());

        Order order = myOrderRepository.findById(orderReturn.getOrderId());

        if (order != null) {
            dto.setOrderDate(order.getOrderDate());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setTotalCount(order.getTotalCount());

            List<OrderItem> items = myOrderItemRepository.findByOrderId(order.getId());
            if (!items.isEmpty()) {
                Product product = items.get(0).getProduct();
                dto.setProduct(toOrderProductDTO(product));
            }
        }

        return dto;
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
            dto.setImage(null);
        }
        return dto;
    }
}