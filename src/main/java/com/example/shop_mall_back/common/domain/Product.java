package com.example.shop_mall_back.common.domain;

import com.example.shop_mall_back.admin.product.domain.DeliveryInfo;
import com.example.shop_mall_back.admin.product.dto.ProductFormDto;
import com.example.shop_mall_back.user.product.domain.Brand;
import com.example.shop_mall_back.user.product.domain.Category;
import com.example.shop_mall_back.user.product.domain.ProductImage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "products")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int price;

    @Builder.Default
    @Column(nullable = false)
    private int stock = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Builder.Default
    @Column(nullable = false)
    private int viewCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "sell_status")
    private SellStatus sellStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private DeliveryInfo deliveryInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    public enum SellStatus {
        판매중, 품절
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    //상품 정보 수정 시 name, price, stock, description, sellStatus 업데이트 메소드
    public void updateProduct(ProductFormDto productFormDto) {
        this.name = productFormDto.getName();
        this.price = productFormDto.getPrice();
        this.stock = productFormDto.getStock();
        this.description = productFormDto.getDescription();
        this.sellStatus = productFormDto.getSellStatus();
    }

    //Brand 정보 업데이트 메소드
    public void changeBrand(Brand brand) {
        this.brand = brand;
    }

    //Category 정보 업데이트 메소드
    public void changeCategory(Category category) {
        this.category = category;
    }

    //DeliveryInfo 정보 업데이트 메소드
    public void changeDeliveryInfo(DeliveryInfo deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }
}
