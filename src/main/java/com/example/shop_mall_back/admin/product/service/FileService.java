package com.example.shop_mall_back.admin.product.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        //UUID (Universally Unique Identifier) : 서로 다른 개체들을 구별하기 위해서 이름을 부여할 때 사용
        UUID uuid = UUID.randomUUID();

        //원본 이미지에서 마지막 점 위치를 찾아내어 그 위치부터 마지막까지
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        //UUID로 받은 값과 원래 파일의 이름의 확장자를 조합해 저장될 파일 이름 만들기
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        //FileOutputStream: 바이트 단위의 출력을 내보낸다.
        //생성자로 파일이 저장될 위치와 파일의 이름을 넘겨 파일에 쓸 파일 출력 스트림을 만든다.
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);

        //fileData 데이터를 파일 출력 스트림에 입력
        fos.write(fileData);
        fos.close();

        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);
        if(deleteFile.exists()){
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else{
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
