package com.example.shop_mall_back.admin.product;

import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.common.domain.ProductImg;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
    private final ProductService productService;

    public Long saveProduct(ProductFormDto productFormDto, List<MultipartFile> productImgFileList) throws Exception{

        //상품 등록
        Product product = productFormDto.createProduct();

        //상품 데이터 저장
        productRepository.save(product);

        //이미지 등록
        for(int i=0;i<productImgFileList.size();i++){
            ProductImg productImg = new ProductImg();
            //여기부터 코딩 이어서
        }

        return null;
    }

}
