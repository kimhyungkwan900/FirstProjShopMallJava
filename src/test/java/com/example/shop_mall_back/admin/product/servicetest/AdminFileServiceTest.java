package com.example.shop_mall_back.admin.product.servicetest;

import com.example.shop_mall_back.admin.product.service.AdminFileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.SimpleTimeZone;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AdminFileServiceTest {

    private final AdminFileService adminFileService = new AdminFileService();

    @Test
    @DisplayName("파일 업로드 성공")
    void upload_File_Success(@TempDir Path tempDir) throws Exception {
        //given
        String originalFileName = "test_image.png";
        String uploadPath = tempDir.toString();
        byte[] fileData = "Image File Data".getBytes();

        //when
        String savedFileName = adminFileService.uploadFile(uploadPath, originalFileName, fileData);

        //then
        Path path = tempDir.resolve(savedFileName);
        assertTrue(Files.exists(path));

        byte[] savedData = Files.readAllBytes(path);
        assertThat(savedData).isEqualTo(fileData);

        assertThat(savedFileName).endsWith(".png");
    }

    @Test
    void deleteFile(@TempDir Path tempDir) throws Exception {
        // given
        String fileName = UUID.randomUUID() + ".png";
        Path path = tempDir.resolve(fileName);

        // 테스트용 파일 생성
        Files.write(path, "Image File Data".getBytes());

        assertThat(Files.exists(path)).isTrue();

        // when
        adminFileService.deleteFile(path.toString());

        // then
        assertThat(Files.exists(path)).isFalse();

    }
}