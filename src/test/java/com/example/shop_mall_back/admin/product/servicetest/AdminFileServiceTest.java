package com.example.shop_mall_back.admin.product.servicetest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AdminFileServiceTest {

    @Test
    void uploadFileTest(@TempDir Path tempDir) throws Exception {
        //given
        String originalFileName = "test_image.png";
        String uploadPath = tempDir.toString();
        byte[] fileData = "Image File Data".getBytes();


        //when


        //then
//        fos.write(fileData);
//        fos.close();
    }

    @Test
    void deleteFile() {
    }
}