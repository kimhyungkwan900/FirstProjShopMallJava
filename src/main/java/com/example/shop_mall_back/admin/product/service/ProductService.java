package com.example.shop_mall_back.admin.product.service;

import com.example.shop_mall_back.admin.product.dto.ProductDto;
import com.example.shop_mall_back.admin.product.dto.ProductFormDto;
import com.example.shop_mall_back.admin.product.repository.ProductImgRepository;
import com.example.shop_mall_back.admin.product.repository.ProductRepository;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.product.domain.Brand;
import com.example.shop_mall_back.user.product.domain.Category;
import com.example.shop_mall_back.user.product.domain.ProductImage;
import com.example.shop_mall_back.user.product.repository.BrandRepository;
import com.example.shop_mall_back.user.product.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    private final ProductImgService productImgService;
    private final ModelMapper modelMapper;

    //상품 등록
    public Long saveProduct(ProductFormDto productFormDto, List<MultipartFile> productImgFileList) throws Exception{

        Product product = productFormDto.createProduct();
        //여기에 brand, category 정보는 데이터베이스에서 가져와서 저장하는거로 수정

        //상품 데이터 저장
        productRepository.save(product);

        //이미지 등록
        for(int i=0;i<productImgFileList.size();i++){
            ProductImage productImage;

            if(i == 0)
                productImage = new ProductImage(product, true);
            else
                productImage = new ProductImage(product, false);

            productImgService.saveProductImg(productImage, productImgFileList.get(i));
        }

        return product.getId();
    }

    //상품 전체조회
    public List<ProductDto> getProductList(){
        List<Product> productList = productRepository.findAll();

        return productList.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    //상품 상세조회
//    @Transactional(readOnly = true)
//    public Product getProductDetail(Long productId){
//        List<>
//    }

    //상품 수정
    public Long updateProduct(ProductFormDto productFormDto, List<MultipartFile> productImgFileList) throws Exception{
        //수정한 카테고리, 브랜드 ID를 이용해 데이터베이스에서 정보 가져오기
        Category category = categoryRepository.findById(productFormDto.getCategoryId())
                .orElseThrow(EntityNotFoundException::new);

        Brand brand = brandRepository.findById(productFormDto.getBrandId())
                .orElseThrow(EntityNotFoundException::new);

        //상품 등록 화면으로부터 받은 상품 아이디를 이용하여 상품 엔티티를 조회
        Product product = productRepository.findById(productFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        //상품 수정 화면으로부터 전달받은 ItemFormDto를 통해 상품 엔티티 업데이트
        product.updateProduct(productFormDto);
        product.changeCategory(category);
        product.changeBrand(brand);

        //상품 아이디 리스트 조회


        //이미지 수정


        return null;
    }

    //조회 조건과 페이지 정보를 받아서 상품 데이터 조회


    //메인 페이지에 보여줄 상품 데이터를 조회


    //상품 삭제
}
