//package com.example.shop_mall_back.admin.product.repositorytest;
//
//
//import com.example.shop_mall_back.admin.product.dto.ProductDto;
//import com.example.shop_mall_back.admin.product.dto.ProductSearchDto;
//import com.example.shop_mall_back.admin.product.repository.AdminProductRepository;
//import com.example.shop_mall_back.admin.product.service.AdminProductService;
//import com.example.shop_mall_back.common.domain.Product;
//import com.example.shop_mall_back.common.domain.QProduct;
//import com.example.shop_mall_back.user.product.domain.Brand;
//import com.example.shop_mall_back.user.product.domain.Category;
//import com.querydsl.core.BooleanBuilder;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import static org.assertj.core.api.Assertions.assertThat;
//import org.assertj.core.util.Arrays;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@DataJpaTest
//class AdminProductRepositoryTest {
//
//    @InjectMocks
//    private AdminProductRepository adminProductRepository;
//
//    @Mock
//    private JPAQueryFactory mockQueryFactory;
//
//    private Brand brandA;
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        brandA = Brand.builder().id(1L).name("<UNK>").build();
//    }
//
//    @Test
//    public void testGetProductPageByCondition() {
//        //given
//        ProductSearchDto productSearchDto = new ProductSearchDto(
//                null,
//                "테스트상품",
//                null,
//                null,
//                1L,
//                null,
//                null,
//                null
//        );
//
//
//        Pageable pageable = PageRequest.of(0, 10);
//
//        QProduct product = QProduct.product;
//
//        List<Product> mockProducts = Arrays.asList(
//                new Product(1L, "테스트상품", null, Product.SellStatus.판매중, new Category(1, "패션의류/잡화", null, null), LocalDateTime.now(), LocalDateTime.now())
//        );
//
//        // fetch mock
//        when(queryFactory.selectFrom(product))
//                .thenReturn(mock(JPAQueryFactory.class));
//        JPAQueryFactory selectQuery = mock(JPAQueryFactory.class);
//        when(queryFactory.selectFrom(product)).thenReturn(selectQuery);
//        when(selectQuery.where(any(BooleanBuilder.class))).thenReturn(selectQuery);
//        when(selectQuery.offset(anyLong())).thenReturn(selectQuery);
//        when(selectQuery.limit(anyLong())).thenReturn(selectQuery);
//        when(selectQuery.fetch()).thenReturn(mockProducts);
//
//        // count mock
//        JPAQueryFactory countQuery = mock(JPAQueryFactory.class);
//        when(queryFactory.select(product.count())).thenReturn(countQuery);
//        when(countQuery.from(product)).thenReturn(countQuery);
//        when(countQuery.where(any(BooleanBuilder.class))).thenReturn(countQuery);
//        when(countQuery.fetchOne()).thenReturn((long) mockProducts.size());
//
//        // when
//        Page<ProductDto> resultPage = adminProductService.getAdminProductPage(productSearchDto, pageable);
//
//        // then
//        assertThat(resultPage).isNotNull();
//        assertThat(resultPage.getContent()).hasSize(1);
//        assertThat(resultPage.getContent().get(0).getName()).contains("테스트상품");
//        assertThat(resultPage.getTotalElements()).isEqualTo(1);
//    }
//    }
//}