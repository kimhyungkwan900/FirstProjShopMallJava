package com.example.shop_mall_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ShopMallBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopMallBackApplication.class, args);
    }

}
