package com.example.shop_mall_back.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = -1073894572L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProduct product = new QProduct("product");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final com.example.shop_mall_back.user.product.domain.QBrand brand;

    public final com.example.shop_mall_back.user.product.domain.QCategory category;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final com.example.shop_mall_back.admin.product.domain.QDeliveryInfo deliveryInfo;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.example.shop_mall_back.user.product.domain.ProductImage, com.example.shop_mall_back.user.product.domain.QProductImage> images = this.<com.example.shop_mall_back.user.product.domain.ProductImage, com.example.shop_mall_back.user.product.domain.QProductImage>createList("images", com.example.shop_mall_back.user.product.domain.ProductImage.class, com.example.shop_mall_back.user.product.domain.QProductImage.class, PathInits.DIRECT2);

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regTime = _super.regTime;

    public final EnumPath<Product.SellStatus> sellStatus = createEnum("sellStatus", Product.SellStatus.class);

    public final NumberPath<Integer> stock = createNumber("stock", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime = _super.updateTime;

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QProduct(String variable) {
        this(Product.class, forVariable(variable), INITS);
    }

    public QProduct(Path<? extends Product> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProduct(PathMetadata metadata, PathInits inits) {
        this(Product.class, metadata, inits);
    }

    public QProduct(Class<? extends Product> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.brand = inits.isInitialized("brand") ? new com.example.shop_mall_back.user.product.domain.QBrand(forProperty("brand")) : null;
        this.category = inits.isInitialized("category") ? new com.example.shop_mall_back.user.product.domain.QCategory(forProperty("category"), inits.get("category")) : null;
        this.deliveryInfo = inits.isInitialized("deliveryInfo") ? new com.example.shop_mall_back.admin.product.domain.QDeliveryInfo(forProperty("deliveryInfo")) : null;
    }

}

