package com.example.shop_mall_back.admin.order.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCustomerClaim is a Querydsl query type for CustomerClaim
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCustomerClaim extends EntityPathBase<CustomerClaim> {

    private static final long serialVersionUID = -759721721L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCustomerClaim customerClaim = new QCustomerClaim("customerClaim");

    public final EnumPath<CustomerClaim.ClaimType> claimType = createEnum("claimType", CustomerClaim.ClaimType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.shop_mall_back.common.domain.QOrder order;

    public QCustomerClaim(String variable) {
        this(CustomerClaim.class, forVariable(variable), INITS);
    }

    public QCustomerClaim(Path<? extends CustomerClaim> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCustomerClaim(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCustomerClaim(PathMetadata metadata, PathInits inits) {
        this(CustomerClaim.class, metadata, inits);
    }

    public QCustomerClaim(Class<? extends CustomerClaim> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new com.example.shop_mall_back.common.domain.QOrder(forProperty("order"), inits.get("order")) : null;
    }

}

