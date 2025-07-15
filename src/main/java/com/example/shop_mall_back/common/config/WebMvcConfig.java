package com.example.shop_mall_back.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}") // 예: file:///C:/shopmall/
    private String uploadPath;

    @Value("${itemImgLocation}") // 예: C:/shopmall/item
    private String itemImgLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // ✅ 새로 등록되는 UUID 기반 이미지
        registry.addResourceHandler("/images/item/**")
                .addResourceLocations("file:///" + itemImgLocation.replace("\\", "/") + "/");

        // ✅ 기존 정적 더미 이미지 경로들 (카테고리별)
        registry.addResourceHandler("/images/footer/**")
                .addResourceLocations(uploadPath + "footer/");

        registry.addResourceHandler("/images/banners/**")
                .addResourceLocations(uploadPath + "banners/");

        registry.addResourceHandler("/images/dress/**")
                .addResourceLocations(uploadPath + "dress/");

        registry.addResourceHandler("/images/blouse/**")
                .addResourceLocations(uploadPath + "blouse/");

        registry.addResourceHandler("/images/sweater/**")
                .addResourceLocations(uploadPath + "sweater/");

        registry.addResourceHandler("/images/coat/**")
                .addResourceLocations(uploadPath + "coat/");

        registry.addResourceHandler("/images/tshirt/**")
                .addResourceLocations(uploadPath + "tshirt/");

        registry.addResourceHandler("/images/sweatshirt/**")
                .addResourceLocations(uploadPath + "sweatshirt/");

        registry.addResourceHandler("/images/shirt/**")
                .addResourceLocations(uploadPath + "shirt/");

        registry.addResourceHandler("/images/jeans/**")
                .addResourceLocations(uploadPath + "jeans/");

        registry.addResourceHandler("/images/bag/**")
                .addResourceLocations(uploadPath + "bag/");

        registry.addResourceHandler("/images/wallet/**")
                .addResourceLocations(uploadPath + "wallet/");

        registry.addResourceHandler("/images/hat/**")
                .addResourceLocations(uploadPath + "hat/");

        registry.addResourceHandler("/images/laptop/**")
                .addResourceLocations(uploadPath + "laptop/");

        registry.addResourceHandler("/images/phone/**")
                .addResourceLocations(uploadPath + "phone/");

        registry.addResourceHandler("/images/earbird/**")
                .addResourceLocations(uploadPath + "earbird/");

        registry.addResourceHandler("/images/monitor/**")
                .addResourceLocations(uploadPath + "monitor/");

        registry.addResourceHandler("/images/smartwatch/**")
                .addResourceLocations(uploadPath + "smartwatch/");

        registry.addResourceHandler("/images/skincare/**")
                .addResourceLocations(uploadPath + "skincare/");

        registry.addResourceHandler("/images/makeup/**")
                .addResourceLocations(uploadPath + "makeup/");

        registry.addResourceHandler("/images/perfume/**")
                .addResourceLocations(uploadPath + "perfume/");

        registry.addResourceHandler("/images/bodycare/**")
                .addResourceLocations(uploadPath + "bodycare/");

        registry.addResourceHandler("/images/fruits/**")
                .addResourceLocations(uploadPath + "fruits/");

        registry.addResourceHandler("/images/vegetables/**")
                .addResourceLocations(uploadPath + "vegetables/");

        registry.addResourceHandler("/images/meat_egg/**")
                .addResourceLocations(uploadPath + "meat_egg/");

        registry.addResourceHandler("/images/simplefoods/**")
                .addResourceLocations(uploadPath + "simplefoods/");

        registry.addResourceHandler("/images/noodle/**")
                .addResourceLocations(uploadPath + "noodle/");

        registry.addResourceHandler("/images/snack/**")
                .addResourceLocations(uploadPath + "snack/");

        registry.addResourceHandler("/images/cleaning/**")
                .addResourceLocations(uploadPath + "cleaning/");

        registry.addResourceHandler("/images/kitchen/**")
                .addResourceLocations(uploadPath + "kitchen/");

        registry.addResourceHandler("/images/bathroom/**")
                .addResourceLocations(uploadPath + "bathroom/");

        registry.addResourceHandler("/images/storage/**")
                .addResourceLocations(uploadPath + "storage/");

        registry.addResourceHandler("/images/babyclothes/**")
                .addResourceLocations(uploadPath + "babyclothes/");

        registry.addResourceHandler("/images/babyfoods/**")
                .addResourceLocations(uploadPath + "babyfoods/");

        registry.addResourceHandler("/images/toys/**")
                .addResourceLocations(uploadPath + "toys/");

        registry.addResourceHandler("/images/stroller/**")
                .addResourceLocations(uploadPath + "stroller/");
    }
}
