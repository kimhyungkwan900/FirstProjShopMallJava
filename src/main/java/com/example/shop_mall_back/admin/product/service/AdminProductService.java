package com.example.shop_mall_back.admin.product.service;

import com.example.shop_mall_back.admin.product.domain.DeliveryInfo;
import com.example.shop_mall_back.admin.product.dto.*;
import com.example.shop_mall_back.admin.product.repository.AdminProductImgRepository;
import com.example.shop_mall_back.admin.product.repository.AdminProductRepository;
import com.example.shop_mall_back.admin.product.repository.DeliveryInfoRepository;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.product.domain.Brand;
import com.example.shop_mall_back.user.product.domain.Category;
import com.example.shop_mall_back.user.product.domain.ProductImage;
import com.example.shop_mall_back.user.product.repository.BrandRepository;
import com.example.shop_mall_back.user.product.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminProductService {

    private final AdminProductRepository adminProductRepository;
    private final AdminProductImgRepository adminProductImgRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final DeliveryInfoRepository deliveryInfoRepository;

    private final ProductImgService productImgService;
    private final ModelMapper modelMapper;

    //---상품 등록
    public Long saveProduct(ProductFormDto productFormDto, List<MultipartFile> productImgFileList) throws Exception{

        Product product = productFormDto.createProduct();
        Brand newBrand = brandRepository.findById(productFormDto.getBrandId())
                .orElseThrow(EntityNotFoundException::new);
        Category newCategory = categoryRepository.findById(productFormDto.getCategoryId())
                .orElseThrow(EntityNotFoundException::new);
        DeliveryInfo newDeliveryInfo = deliveryInfoRepository.findById(productFormDto.getDeliveryInfoId())
                .orElseThrow(EntityNotFoundException::new);

        product.changeBrand(newBrand);
        product.changeCategory(newCategory);
        product.changeDeliveryInfo(newDeliveryInfo);

        //상품 데이터 저장
        adminProductRepository.save(product);

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

    //---조회 조건과 페이지 정보를 받아서 상품 데이터 조회
    @Transactional(readOnly = true)
    public Page<ProductDto> getAdminItemPage(ProductSearchDto productSearchDto, Pageable pageable){
        return adminProductRepository.getProductPageByCondition(productSearchDto, pageable)
                .map(product -> modelMapper.map(product, ProductDto.class));
    }

    //---상품 상세정보 조회
    @Transactional(readOnly = true)
    public ProductDetailDto getProductDetail(Long productId){
        List<ProductImage> productImgList = adminProductImgRepository.findByItemIdOrderByIdAsc(productId);
        List<ProductImgDto> productImgDtoList = new ArrayList<>();

        for(ProductImage productImage : productImgList){
            ProductImgDto productImgDto = ProductImgDto.of(productImage);
            productImgDtoList.add(productImgDto);
        }

        Product product = adminProductRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);

        ProductDetailDto productDetailDto = modelMapper.map(product, ProductDetailDto.class);
        productDetailDto.setProductImgDtoList(productImgDtoList);

        return productDetailDto;
    }

    //---상품 수정
    public Long updateProduct(ProductFormDto productFormDto, List<MultipartFile> productImgFileList) throws Exception{
        //수정한 카테고리 ID, 브랜드 ID를 이용해 데이터베이스에서 정보 가져오기
        Category category = categoryRepository.findById(productFormDto.getCategoryId())
                .orElseThrow(EntityNotFoundException::new);

        Brand brand = brandRepository.findById(productFormDto.getBrandId())
                .orElseThrow(EntityNotFoundException::new);

        //상품 등록 화면으로부터 받은 상품 아이디를 이용하여 상품 엔티티를 조회
        Product product = adminProductRepository.findById(productFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        //상품 수정 화면으로부터 전달받은 productFormDto를 통해 상품 엔티티 업데이트
        product.updateProduct(productFormDto);
        product.changeCategory(category);
        product.changeBrand(brand);

        //상품 이미지 아이디 리스트 조회
        List<Long> productImgIds = productFormDto.getProductImgIds();

        //이미지 수정
        for(int i=0;i<productImgFileList.size();i++){
            productImgService.updateProductImg(productImgIds.get(i), productImgFileList.get(i));
        }

        return product.getId();
    }

    //---상품 삭제
    public void deleteProduct(Long productId){
        adminProductRepository.deleteById(productId);
    }
}
