package com.example.shop_mall_back.admin.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.modelmapper.ModelMapper;

@Getter
@AllArgsConstructor
@Builder
public class BrandDto {
    private Long id;

    private String name;

    private ModelMapper modelMapper = new ModelMapper();


}
