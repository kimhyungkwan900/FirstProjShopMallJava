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

        // 파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = adminFileService.uploadFile(productImgLocation, oriImgName, productImgFile.getBytes());

            // ✅ 브라우저 접근 가능한 경로로 imgUrl 설정
            imgUrl = "/images/item/" + imgName;
        }

        // DB에 저장
        productImage.updateProductImg(oriImgName, imgName, imgUrl);
        adminProductImgRepository.save(productImage);
    }


    //상품 이미지 수정
    public void updateProductImg(Long productImgId, MultipartFile productImgFile) throws Exception {

        if(!productImgFile.isEmpty()){
            ProductImage savedProductImg = adminProductImgRepository.findById(productImgId)
                    .orElseThrow(EntityNotFoundException::new);

            // 기존 이미지 삭제
            if(!StringUtils.isEmpty(savedProductImg.getImgName())){
                adminFileService.deleteFile(productImgLocation + "/" + savedProductImg.getImgName());
            }

            String oriImagename = productImgFile.getOriginalFilename();
            String imgName = adminFileService.uploadFile(productImgLocation, oriImagename, productImgFile.getBytes());

            // ✅ 여기서도 브라우저 접근 가능한 URL로 설정
            String imgUrl = "/images/item/" + imgName;

            savedProductImg.updateProductImg(oriImagename, imgName, imgUrl);
        }
    }

}
