package com.example.shop_mall_back.user.Cart.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestockAlarm is a Querydsl query type for RestockAlarm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestockAlarm extends EntityPathBase<RestockAlarm> {

    private static final long serialVersionUID = 794537145L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRestockAlarm restockAlarm = new QRestockAlarm("restockAlarm");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isRequest = createBoolean("isRequest");

    public final com.example.shop_mall_back.common.domain.member.QMember member;

    public final BooleanPath notified = createBoolean("notified");

    public final com.example.shop_mall_back.common.domain.QProduct product;

    public QRestockAlarm(String variable) {
        this(RestockAlarm.class, forVariable(variable), INITS);
    }

    public QRestockAlarm(Path<? extends RestockAlarm> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRestockAlarm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRestockAlarm(PathMetadata metadata, PathInits inits) {
        this(RestockAlarm.class, metadata, inits);
    }

    public QRestockAlarm(Class<? extends RestockAlarm> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.shop_mall_back.common.domain.member.QMember(forProperty("member")) : null;
        this.product = inits.isInitialized("product") ? new com.example.shop_mall_back.common.domain.QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

