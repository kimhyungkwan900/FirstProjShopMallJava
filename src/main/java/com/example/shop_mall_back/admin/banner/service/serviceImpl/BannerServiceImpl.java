package com.example.shop_mall_back.admin.banner.service.serviceImpl;

import com.example.shop_mall_back.admin.banner.domain.Banner;
import com.example.shop_mall_back.admin.banner.dto.BannerDTO;
import com.example.shop_mall_back.admin.banner.dto.BannerRequestDTO;
import com.example.shop_mall_back.admin.banner.repository.BannerRepository;
import com.example.shop_mall_back.admin.banner.service.serviceInterface.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;

    @Override
    public List<BannerDTO> getVisibleBanners() {
        return bannerRepository.findVisibleBanners()
                .stream()
                .map(b -> BannerDTO.builder()
                        .id(b.getId())
                        .imageUrl(b.getImageUrl())
                        .link(b.getLink())
                        .alt(b.getAlt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Long createBanner(BannerRequestDTO dto) {
        Banner banner = dtoToBanner(dto);
        return bannerRepository.save(banner).getId();
    }

    @Override
    public void updateBanner(Long id, BannerRequestDTO dto) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 배너가 존재하지 않습니다."));

        banner.setImageUrl(dto.getImageUrl());
        banner.setLink(dto.getLink());
        banner.setAlt(dto.getAlt());
        banner.setDisplayOrder(dto.getDisplayOrder());
        banner.setActive(dto.isActive());
        banner.setVisibleFrom(dto.getVisibleFrom());
        banner.setVisibleTo(dto.getVisibleTo());

        bannerRepository.save(banner);
    }

    @Override
    public void deleteBanner(Long id) {
        if (!bannerRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제할 배너가 존재하지 않습니다.");
        }
        bannerRepository.deleteById(id);
    }

    @Override
    public BannerRequestDTO getBannerById(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("배너가 존재하지 않습니다."));

        return entityToRequestDTO(banner);
    }
}
