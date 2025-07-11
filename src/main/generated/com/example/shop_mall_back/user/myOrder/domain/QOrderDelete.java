package com.example.shop_mall_back.user.myOrder.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrderDelete is a Querydsl query type for OrderDelete
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderDelete extends EntityPathBase<OrderDelete> {

    private static final long serialVersionUID = 566962442L;

    public static final QOrderDelete orderDelete = new QOrderDelete("orderDelete");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public QOrderDelete(String variable) {
        super(OrderDelete.class, forVariable(variable));
    }

    public QOrderDelete(Path<? extends OrderDelete> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrderDelete(PathMetadata metadata) {
        super(OrderDelete.class, metadata);
    }

}

