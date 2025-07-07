package com.example.shop_mall_back.user.Order.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrderReturn is a Querydsl query type for OrderReturn
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderReturn extends EntityPathBase<OrderReturn> {

    private static final long serialVersionUID = -322249053L;

    public static final QOrderReturn orderReturn = new QOrderReturn("orderReturn");

    public final StringPath detail = createString("detail");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final StringPath reason = createString("reason");

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final EnumPath<OrderReturn.ReturnType> returnType = createEnum("returnType", OrderReturn.ReturnType.class);

    public QOrderReturn(String variable) {
        super(OrderReturn.class, forVariable(variable));
    }

    public QOrderReturn(Path<? extends OrderReturn> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrderReturn(PathMetadata metadata) {
        super(OrderReturn.class, metadata);
    }

}

