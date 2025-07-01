package com.example.shop_mall_back.admin.product.dto;

import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.product.domain.Brand;
import com.example.shop_mall_back.user.product.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProductFormDto {
    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String name;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
    private String description;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stock;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Long categoryId;

    private Long brandId;

    //배송정보
    private Long deliveryInfoId;

    private Product.SellStatus sellStatus;

    private List<ProductImgDto> productImgDtoList = new ArrayList<>();

    private List<Long> productImgIds = new ArrayList<>();


    //--------- 직접 코딩 하기
    private static ModelMapper modelMapper = new ModelMapper();

    public Product createProduct(){
        return modelMapper.map(this, Product.class);
    }

    public static ProductFormDto of(Product item){
        return modelMapper.map(item,ProductFormDto.class);
    }
}
