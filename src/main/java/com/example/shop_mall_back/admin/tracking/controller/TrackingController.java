package com.example.shop_mall_back.admin.tracking.controller;

import com.example.shop_mall_back.admin.tracking.dto.TrackingInfoDTO;
import com.example.shop_mall_back.admin.tracking.service.TrackingInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingInfoService trackingInfoService;

    @PostMapping("/order/trackingInput")
    public ResponseEntity<String> trackingInput(@RequestBody TrackingInfoDTO trackingInfoDTO) {
        trackingInfoService.addTrackingInfo(trackingInfoDTO);
        return ResponseEntity.ok().build();
    }
}