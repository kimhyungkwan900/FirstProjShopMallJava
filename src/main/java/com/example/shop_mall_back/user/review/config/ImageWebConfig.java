package com.example.shop_mall_back.user.review.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class ImageWebConfig implements WebMvcConfigurer {

    @Value("${reviewImgLocation}")
    private String reviewImgLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = Paths.get(reviewImgLocation).toUri().toString();

        registry.addResourceHandler("/images/review/**")
                .addResourceLocations(uploadPath);
    }
}
