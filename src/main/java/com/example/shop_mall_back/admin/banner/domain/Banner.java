package com.example.shop_mall_back.admin.banner.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "banners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "image_url")
    private String imageUrl;

    private String link;

    // front alt 값
    private String alt;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "visible_from")
    private LocalDateTime visibleFrom;

    @Column(name = "visible_to")
    private LocalDateTime visibleTo;
}
