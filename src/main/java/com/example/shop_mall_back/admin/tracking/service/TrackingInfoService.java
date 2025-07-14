package com.example.shop_mall_back.admin.tracking.service;

import com.example.shop_mall_back.admin.tracking.domain.TrackingInfo;
import com.example.shop_mall_back.admin.tracking.dto.TrackingInfoDTO;
import com.example.shop_mall_back.admin.tracking.repository.TrackingInfoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackingInfoService {

    private final TrackingInfoRepository trackingInfoRepository;
    private final ModelMapper modelMapper;

    public void addTrackingInfo(TrackingInfoDTO trackingInfoDTO){
        TrackingInfo trackingInfo = modelMapper.map(trackingInfoDTO, TrackingInfo.class);
        trackingInfoRepository.save(trackingInfo);
    }
}