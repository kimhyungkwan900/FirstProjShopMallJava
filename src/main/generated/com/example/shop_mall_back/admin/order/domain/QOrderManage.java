package com.example.shop_mall_back.admin.order.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderManage is a Querydsl query type for OrderManage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderManage extends EntityPathBase<OrderManage> {

    private static final long serialVersionUID = -50140516L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderManage orderManage = new QOrderManage("orderManage");

    public final com.example.shop_mall_back.common.domain.QBaseEntity _super = new com.example.shop_mall_back.common.domain.QBaseEntity(this);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final com.example.shop_mall_back.common.domain.QOrder order;

    public final EnumPath<OrderManage.OrderStatus> orderStatus = createEnum("orderStatus", OrderManage.OrderStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regTime = _super.regTime;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime = _super.updateTime;

    public QOrderManage(String variable) {
        this(OrderManage.class, forVariable(variable), INITS);
    }

    public QOrderManage(Path<? extends OrderManage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderManage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderManage(PathMetadata metadata, PathInits inits) {
        this(OrderManage.class, metadata, inits);
    }

    public QOrderManage(Class<? extends OrderManage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new com.example.shop_mall_back.common.domain.QOrder(forProperty("order"), inits.get("order")) : null;
    }

}

