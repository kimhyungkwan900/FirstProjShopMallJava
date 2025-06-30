package com.example.shop_mall_back.user.review.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@Log4j2
public class FileService {

    private final Path rootLocation = Paths.get("uploads/review");

    /**
     * 파일 저장 및 경로 반환
     */
    public String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일은 저장할 수 없습니다.");
        }

        try {
            // 디렉토리 없으면 생성
            Files.createDirectories(rootLocation);

            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".")); // 확장자 추출
            String filename = UUID.randomUUID() + ext; // 중복 방지 이름 생성

            Path destination = rootLocation.resolve(filename);
            file.transferTo(destination.toFile());

            String savedPath = "/uploads/review/" + filename; // 클라이언트 응답용
            log.info("파일 저장 완료: {}", savedPath);
            return savedPath;

        } catch (IOException e) {
            log.error("파일 저장 실패", e);
            throw new RuntimeException("파일 저장 실패", e);
        }
    }

    /**
     * 파일 삭제
     */
    public void deleteFile(String filePath) {
        try {
            Path pathToDelete = Paths.get("uploads").resolve(filePath.replace("/uploads/", ""));
            Files.deleteIfExists(pathToDelete);
            log.info("파일 삭제 성공: {}", filePath);
        } catch (IOException e) {
            log.error("파일 삭제 실패", e);
            throw new RuntimeException("파일 삭제 실패", e);
        }
    }
}