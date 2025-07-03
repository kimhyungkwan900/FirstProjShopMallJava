package com.example.shop_mall_back.user.Cart.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDeliveryFeeRule is a Querydsl query type for DeliveryFeeRule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDeliveryFeeRule extends EntityPathBase<DeliveryFeeRule> {

    private static final long serialVersionUID = 1189828547L;

    public static final QDeliveryFeeRule deliveryFeeRule = new QDeliveryFeeRule("deliveryFeeRule");

    public final NumberPath<Integer> deliveryFee = createNumber("deliveryFee", Integer.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> minOrderAmount = createNumber("minOrderAmount", Integer.class);

    public QDeliveryFeeRule(String variable) {
        super(DeliveryFeeRule.class, forVariable(variable));
    }

    public QDeliveryFeeRule(Path<? extends DeliveryFeeRule> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDeliveryFeeRule(PathMetadata metadata) {
        super(DeliveryFeeRule.class, metadata);
    }

}

