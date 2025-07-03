package com.example.shop_mall_back.user.Cart.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCartItem is a Querydsl query type for CartItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCartItem extends EntityPathBase<CartItem> {

    private static final long serialVersionUID = -1161086658L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCartItem cartItem = new QCartItem("cartItem");

    public final com.example.shop_mall_back.common.domain.QCart cart;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isSelected = createBoolean("isSelected");

    public final BooleanPath isSoldOut = createBoolean("isSoldOut");

    public final com.example.shop_mall_back.common.domain.QProduct product;

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final StringPath selectedOption = createString("selectedOption");

    public QCartItem(String variable) {
        this(CartItem.class, forVariable(variable), INITS);
    }

    public QCartItem(Path<? extends CartItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCartItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCartItem(PathMetadata metadata, PathInits inits) {
        this(CartItem.class, metadata, inits);
    }

    public QCartItem(Class<? extends CartItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cart = inits.isInitialized("cart") ? new com.example.shop_mall_back.common.domain.QCart(forProperty("cart"), inits.get("cart")) : null;
        this.product = inits.isInitialized("product") ? new com.example.shop_mall_back.common.domain.QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

