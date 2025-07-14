package com.example.shop_mall_back.admin.order.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClaimManage is a Querydsl query type for ClaimManage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClaimManage extends EntityPathBase<ClaimManage> {

    private static final long serialVersionUID = 583672074L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClaimManage claimManage = new QClaimManage("claimManage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.shop_mall_back.user.myOrder.domain.QOrderReturn orderReturn;

    public QClaimManage(String variable) {
        this(ClaimManage.class, forVariable(variable), INITS);
    }

    public QClaimManage(Path<? extends ClaimManage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClaimManage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClaimManage(PathMetadata metadata, PathInits inits) {
        this(ClaimManage.class, metadata, inits);
    }

    public QClaimManage(Class<? extends ClaimManage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.orderReturn = inits.isInitialized("orderReturn") ? new com.example.shop_mall_back.user.myOrder.domain.QOrderReturn(forProperty("orderReturn")) : null;
    }

}

