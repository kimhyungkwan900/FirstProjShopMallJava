package com.example.shop_mall_back.admin.product.service;

import com.example.shop_mall_back.admin.product.repository.AdminProductImgRepository;
import com.example.shop_mall_back.user.product.domain.ProductImage;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductImgService {

    //application.properties의 itemImgLocation 불러오기
    @Value("${itemImgLocation}")
    String productImgLocation;

    private final AdminProductImgRepository adminProductImgRepository;
    private final AdminFileService adminFileService;


    //상품 이미지 등록
    public void saveProductImg(ProductImage productImage, MultipartFile productImgFile) throws Exception{

        String oriImgName = productImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";
        //oriImgName : 업로드했던 상품 이미지의 원래 이름
        //imgName : 실제 로컬에 저장된 상품 이미 파일의 이름
        //imgUrl : 업로드 결과 로컬에 저장된 파일을 불러오는 경로

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = adminFileService.uploadFile(productImgLocation, oriImgName, productImgFile.getBytes());

            //저장한 상품 이미지를 불러올 경로 설정
            imgUrl = "/images/item/" + imgName;
        }

        //상품 이미지 정보 저장
        productImage.updateProductImg(oriImgName, imgName, imgUrl);
        adminProductImgRepository.save(productImage);
    }

    //상품 이미지 수정
    public void updateProductImg(Long productImgId, MultipartFile productImgFile) throws Exception{

        //업로드된 이미지 파일이 비어있는지 확인
        if(!productImgFile.isEmpty()){
            ProductImage savedProductImg = adminProductImgRepository.findById(productImgId).orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedProductImg.getImgName())){
                adminFileService.deleteFile(productImgLocation + "/" + savedProductImg.getImgName());
            }

            //새로운 이미지 저장
            String oriImagename = productImgFile.getOriginalFilename();

            String imgName = adminFileService.uploadFile(productImgLocation, oriImagename, productImgFile.getBytes());

            String imgUrl = "/images/item/" + imgName;

            savedProductImg.updateProductImg(oriImagename, imgName, imgUrl);
        }
    }
}
