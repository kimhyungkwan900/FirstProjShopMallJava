package com.example.shop_mall_back.admin.Coupon.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
@Getter
@Setter
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="coupon_code", nullable = false, unique = true)
    private String couponCode;

    @Column(name = "discount_amount")
    private Integer discountAmount;

    @Column(name = "minimum_order_amount")
    private Integer minimumOrderAmount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name= "created_at", updatable = false)
    private LocalDateTime createdAt; //생성일

    @Column(name= "updated_at")
    private LocalDateTime updatedAt; //수정일


    @PrePersist
    private void prePersist(){
        this.createdAt = this.updatedAt = LocalDateTime.now(); //처음 저장할 때 실행되어서 자동저장함
    }

    @PreUpdate
    private void OnUpdate(){
        this.updatedAt = LocalDateTime.now(); //업데이트할 때 실행되어서 updatedAt만 새로 갱신
    }


}
