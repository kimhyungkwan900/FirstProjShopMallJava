package com.example.shop_mall_back.admin.product.domaintest;

import com.example.shop_mall_back.admin.product.domain.DeliveryInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeliveryInfoTest {
    @Test
    @DisplayName("DeliveryInfo가 생성되는지 확인하는 테스트")
    void createDeliverInfo() {
        //given
        DeliveryInfo deliveryInfo = DeliveryInfo.builder()
                .id(1L)
                .delivery_yn("y")
                .deliveryCom(DeliveryInfo.Delivery_com.LOGEN)
                .deliveryPrice(3000)
                .build();

        //when, then
        Assertions.assertThat(deliveryInfo.getId()).isEqualTo(1L);
        Assertions.assertThat(deliveryInfo.getDelivery_yn()).isEqualTo("y");
        Assertions.assertThat(deliveryInfo.getDeliveryCom()).isEqualTo(DeliveryInfo.Delivery_com.LOGEN);
        Assertions.assertThat(deliveryInfo.getDeliveryPrice()).isEqualTo(3000);
    }
}