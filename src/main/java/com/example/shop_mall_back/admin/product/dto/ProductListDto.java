package com.example.shop_mall_back.admin.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
@AllArgsConstructor
public class ProductListDto {
    private Page<ProductDto> products;
    private ProductSearchDto productSearchDto;
    private Integer maxPage;
    private Integer totalPage;
}
