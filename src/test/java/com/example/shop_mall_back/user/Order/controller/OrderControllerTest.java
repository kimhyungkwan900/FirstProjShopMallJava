package com.example.shop_mall_back.user.Order.controller;

import com.example.shop_mall_back.common.config.jwt.TokenProvider;
import com.example.shop_mall_back.user.Order.dto.OrderDto;
import com.example.shop_mall_back.user.Order.dto.OrderItemDto;
import com.example.shop_mall_back.user.Order.dto.OrderSummaryDto;
import com.example.shop_mall_back.user.Order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false) // 시큐리티 필터 비활성화 (인증 없이 테스트 가능)
class OrderControllerTest {

    @MockBean
    private TokenProvider tokenProvider; // JWT 인증 무력화

    @Autowired
    private MockMvc mockMvc; // 컨트롤러 테스트용 MockMvc

    @MockBean
    private OrderService orderService; // 의존성 서비스 Mock

    @Autowired
    private ObjectMapper objectMapper; // JSON 직렬화/역직렬화용

    @Test
    @DisplayName("주문 생성 성공")
    void createOrder_success() throws Exception {
        // given: 주문 요청 DTO 설정
        OrderDto orderDto = new OrderDto();
        orderDto.setMember_id(1L);
        orderDto.setDelivery_address_id(1L);
        orderDto.setOrder_date(java.time.LocalDateTime.now());
        orderDto.setTotal_amount(20000);
        orderDto.setTotal_count(2);
        orderDto.setPayment_method("CREDIT_CARD");
        orderDto.setDelivery_request("문 앞에 두세요");

        // ✅ Mock: 서비스가 OrderSummaryDto 반환하도록 설정
        OrderSummaryDto mockSummary = OrderSummaryDto.builder()
                .orderId(100L)
                .memberName("홍길동")
                .deliveryAddress("서울 강남구 테헤란로 123")
                .paymentMethod("CREDIT_CARD")
                .totalAmount(20000)
                .deliveryFee(3000)
                .orderDate(orderDto.getOrder_date())
                .orderItems(List.of(
                        OrderItemDto.builder()
                                .id(1L)
                                .orderId(100L)
                                .productId(101L)
                                .quantity(2)
                                .price(10000)
                                .productTitle("테스트 상품")
                                .build()
                ))
                .build();
        Mockito.when(orderService.createOrder(eq(1L), any(OrderDto.class)))
                .thenReturn(mockSummary);

        // when & then: POST 요청 결과 검증
        mockMvc.perform(post("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(100))
                .andExpect(jsonPath("$.memberName").value("홍길동"))
                .andExpect(jsonPath("$.totalAmount").value(20000))
                .andExpect(jsonPath("$.orderItems[0].productTitle").value("테스트 상품"));
    }


    @Test
    @DisplayName("결제 성공 처리")
    void completeOrder_success() throws Exception {
        // given: 서비스 Mock 처리
        doNothing().when(orderService).completePayAndOrder(1L);

        // when & then: POST 요청 결과 검증
        mockMvc.perform(post("/api/orders/1/complete"))
                .andExpect(status().isOk())
                .andExpect(content().string("결제 완료, 주문 접수 되었습니다."));
    }

    @Test
    @DisplayName("결제 실패 처리")
    void failedOrder_success() throws Exception {
        // given: 서비스 Mock 처리
        doNothing().when(orderService).cancelPay(1L);

        // when & then: POST 요청 결과 검증
        mockMvc.perform(post("/api/orders/1/failed"))
                .andExpect(status().isOk())
                .andExpect(content().string("결제 실패 처리되었습니다."));
    }

    @Test
    @DisplayName("배송 요청사항 저장")
    void deliveryRequest_success() throws Exception {
        // given: 주문 요청 DTO
        OrderDto orderDto = OrderDto.builder()
                .member_id(1L)
                .delivery_address_id(1L)
                .order_date(java.time.LocalDateTime.now())
                .total_amount(20000)
                .total_count(2)
                .payment_method("CREDIT_CARD")
                .delivery_request("부재 시 경비실에 맡겨주세요")
                .build();

        // ✅ Mock: 서비스가 OrderSummaryDto 반환하도록 설정
        OrderSummaryDto mockSummary = OrderSummaryDto.builder()
                .orderId(101L)
                .memberName("홍길동")
                .deliveryAddress("서울 강남구 테헤란로 123")
                .paymentMethod("CREDIT_CARD")
                .totalAmount(20000)
                .deliveryFee(3000)
                .orderDate(orderDto.getOrder_date())
                .orderItems(List.of())
                .build();
        Mockito.when(orderService.createOrder(eq(1L), any(OrderDto.class)))
                .thenReturn(mockSummary);

        // ✅ Mock: 배송 요청사항 저장은 아무것도 반환하지 않음
        doNothing().when(orderService).saveDeliveryRequestNote(eq(101L), any(OrderDto.class));

        // when & then: POST 요청 결과 검증
        mockMvc.perform(post("/api/orders/deliveryRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(101))
                .andExpect(jsonPath("$.deliveryAddress").value("서울 강남구 테헤란로 123"))
                .andExpect(jsonPath("$.deliveryFee").value(3000));
    }


    @Test
    @DisplayName("결제 처리 - 성공")
    void pay_success() throws Exception {
        // given: 결제 성공 Mock
        doNothing().when(orderService).completePayAndOrder(1L);
        // when & then: POST 요청 결과 검증
        mockMvc.perform(post("/api/orders/1/pay")
                        .param("paymentToken", "TOKEN_VALID"))
                .andExpect(status().isOk())
                .andExpect(content().string("결제가 성공적으로 처리되었습니다."));
    }
}

