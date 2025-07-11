package com.example.shop_mall_back.user.product.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWishlistItemId is a Querydsl query type for WishlistItemId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QWishlistItemId extends BeanPath<WishlistItemId> {

    private static final long serialVersionUID = -1420068401L;

    public static final QWishlistItemId wishlistItemId = new QWishlistItemId("wishlistItemId");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public QWishlistItemId(String variable) {
        super(WishlistItemId.class, forVariable(variable));
    }

    public QWishlistItemId(Path<? extends WishlistItemId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWishlistItemId(PathMetadata metadata) {
        super(WishlistItemId.class, metadata);
    }

}

