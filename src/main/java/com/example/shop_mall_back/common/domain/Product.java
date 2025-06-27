package com.example.shop_mall_back.common.domain;

import com.example.shop_mall_back.user.product.domain.Brand;
import com.example.shop_mall_back.user.product.domain.Category;
import com.example.shop_mall_back.user.product.domain.ProductImage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "products")
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    public enum SellStatus {
        판매중, 품절
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
