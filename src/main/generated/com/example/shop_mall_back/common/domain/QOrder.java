package com.example.shop_mall_back.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = 1803533043L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrder order = new QOrder("order1");

    public final StringPath deliveryRequest = createString("deliveryRequest");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.shop_mall_back.common.domain.member.QMember member;

    public final com.example.shop_mall_back.common.domain.member.QMemberAddress memberAddress;

    public final DateTimePath<java.time.LocalDateTime> orderDate = createDateTime("orderDate", java.time.LocalDateTime.class);

    public final com.example.shop_mall_back.admin.order.domain.QOrderManage orderManage;

    public final StringPath paymentMethod = createString("paymentMethod");

    public final EnumPath<com.example.shop_mall_back.user.Order.constant.PaymentStatus> paymentStatus = createEnum("paymentStatus", com.example.shop_mall_back.user.Order.constant.PaymentStatus.class);

    public final NumberPath<Integer> totalAmount = createNumber("totalAmount", Integer.class);

    public final NumberPath<Integer> totalCount = createNumber("totalCount", Integer.class);

    public QOrder(String variable) {
        this(Order.class, forVariable(variable), INITS);
    }

    public QOrder(Path<? extends Order> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrder(PathMetadata metadata, PathInits inits) {
        this(Order.class, metadata, inits);
    }

    public QOrder(Class<? extends Order> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.shop_mall_back.common.domain.member.QMember(forProperty("member")) : null;
        this.memberAddress = inits.isInitialized("memberAddress") ? new com.example.shop_mall_back.common.domain.member.QMemberAddress(forProperty("memberAddress"), inits.get("memberAddress")) : null;
        this.orderManage = inits.isInitialized("orderManage") ? new com.example.shop_mall_back.admin.order.domain.QOrderManage(forProperty("orderManage"), inits.get("orderManage")) : null;
    }

}

