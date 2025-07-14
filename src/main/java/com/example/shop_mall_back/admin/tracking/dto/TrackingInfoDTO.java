package com.example.shop_mall_back.admin.tracking.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TrackingInfoDTO {
    private Long id;

    private Long orderId;

    private String courierCode;

    private String trackingNumber;
}
