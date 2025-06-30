//package com.example.shop_mall_back.common.domain;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Getter
//@Builder
//@Table(name = "products_img")
//@NoArgsConstructor
//@AllArgsConstructor
//public class ProductImg {
//
//    @Id
//    @Column
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    private String imgName; //이미지 파일명
//
//    private String oriImgName; //원본 이미지 파일명
//
//    private String imgUrl; //이미지 조회 경로
//
//    private String repimgYn; //대표 이미지 여부
//    //메인 페이지에 노출시키기 위해
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")
//    private Product product;
//
//    // 원본이미지 파일명, 업데이트할 이미지 파일명, 이미지 경로
//    public void updateItemImg(String oriImgName, String imgName, String imgUrl){
//        this.oriImgName = oriImgName;
//        this.imgName = imgName;
//        this.imgUrl = imgUrl;
//    }
//}
