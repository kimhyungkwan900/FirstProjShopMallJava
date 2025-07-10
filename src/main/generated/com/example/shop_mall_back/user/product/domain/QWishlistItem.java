package com.example.shop_mall_back.user.product.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWishlistItem is a Querydsl query type for WishlistItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWishlistItem extends EntityPathBase<WishlistItem> {

    private static final long serialVersionUID = -890862188L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWishlistItem wishlistItem = new QWishlistItem("wishlistItem");

    public final QWishlistItemId id;

    public final com.example.shop_mall_back.common.domain.QProduct product;

    public final com.example.shop_mall_back.common.domain.member.QMember user;

    public QWishlistItem(String variable) {
        this(WishlistItem.class, forVariable(variable), INITS);
    }

    public QWishlistItem(Path<? extends WishlistItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWishlistItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWishlistItem(PathMetadata metadata, PathInits inits) {
        this(WishlistItem.class, metadata, inits);
    }

    public QWishlistItem(Class<? extends WishlistItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QWishlistItemId(forProperty("id")) : null;
        this.product = inits.isInitialized("product") ? new com.example.shop_mall_back.common.domain.QProduct(forProperty("product"), inits.get("product")) : null;
        this.user = inits.isInitialized("user") ? new com.example.shop_mall_back.common.domain.member.QMember(forProperty("user")) : null;
    }

}

