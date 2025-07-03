package com.example.shop_mall_back.user.product.service;

import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.repository.MemberRepository;
import com.example.shop_mall_back.user.product.domain.WishlistItem;
import com.example.shop_mall_back.user.product.dto.WishlistItemDto;
import com.example.shop_mall_back.user.product.repository.ProductRepository;
import com.example.shop_mall_back.user.product.repository.WishlistItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WishlistServiceTest {

    @Mock private MemberRepository memberRepository;
    @Mock private ProductRepository productRepository;
    @Mock private WishlistItemRepository wishlistItemRepository;

    @InjectMocks private WishlistService wishlistService;

    private final Long userId = 1L;
    private final Long productId = 10L;
    private Member member;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        member = Member.builder().id(userId).build();
        product = Product.builder().id(productId).build();
    }

    @Test
    void getWishlistByUserId_ShouldReturnDtoList() {
        WishlistItem item = WishlistItem.builder()
                .user(member)
                .product(product)
                .build();
        when(wishlistItemRepository.findByUserId(userId)).thenReturn(List.of(item));

        List<WishlistItemDto> result = wishlistService.getWishlistByUserId(userId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductId()).isEqualTo(productId);
    }

    @Test
    void toggleWishlist_WhenItemExists_ShouldDeleteIt() {
        WishlistItem item = WishlistItem.builder().user(member).product(product).build();
        when(wishlistItemRepository.findByUserIdAndProductId(userId, productId))
                .thenReturn(Optional.of(item));

        wishlistService.toggleWishlist(userId, productId);

        verify(wishlistItemRepository).delete(item);
    }

    @Test
    void toggleWishlist_WhenItemDoesNotExist_ShouldAddIt() {
        when(wishlistItemRepository.findByUserIdAndProductId(userId, productId))
                .thenReturn(Optional.empty());

        wishlistService.toggleWishlist(userId, productId);

        ArgumentCaptor<WishlistItem> captor = ArgumentCaptor.forClass(WishlistItem.class);
        verify(wishlistItemRepository).save(captor.capture());

        WishlistItem saved = captor.getValue();
        assertThat(saved.getUser().getId()).isEqualTo(userId);
        assertThat(saved.getProduct().getId()).isEqualTo(productId);
    }

    @Test
    void existingByUserAndProduct_ShouldReturnTrueIfExists() {
        when(wishlistItemRepository.existsByUserIdAndProductId(userId, productId))
                .thenReturn(true);

        boolean exists = wishlistService.existingByUserAndProduct(userId, productId);
        assertThat(exists).isTrue();
    }

    @Test
    void addToWishlist_WhenAlreadyExists_ShouldNotSave() {
        when(wishlistItemRepository.existsByUserIdAndProductId(userId, productId))
                .thenReturn(true);

        wishlistService.addToWishlist(userId, productId);

        verify(wishlistItemRepository, never()).save(any());
    }

    @Test
    void addToWishlist_WhenNotExists_ShouldSaveItem() {
        when(wishlistItemRepository.existsByUserIdAndProductId(userId, productId)).thenReturn(false);
        when(memberRepository.findById(userId)).thenReturn(Optional.of(member));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        wishlistService.addToWishlist(userId, productId);

        ArgumentCaptor<WishlistItem> captor = ArgumentCaptor.forClass(WishlistItem.class);
        verify(wishlistItemRepository).save(captor.capture());

        WishlistItem saved = captor.getValue();
        assertThat(saved.getUser()).isEqualTo(member);
        assertThat(saved.getProduct()).isEqualTo(product);
    }
}
