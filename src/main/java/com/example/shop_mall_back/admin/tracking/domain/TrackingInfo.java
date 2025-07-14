package com.example.shop_mall_back.admin.tracking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Tracking_info")
@Getter
@Setter
public class TrackingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "courier_code")
    private String courierCode;

    @Column(name = "tracking_number")
    private String trackingNumber;

}
