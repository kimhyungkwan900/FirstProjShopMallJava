package com.example.shop_mall_back.admin.product.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDeliveryInfo is a Querydsl query type for DeliveryInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDeliveryInfo extends EntityPathBase<DeliveryInfo> {

    private static final long serialVersionUID = 709008250L;

    public static final QDeliveryInfo deliveryInfo = new QDeliveryInfo("deliveryInfo");

    public final StringPath delivery_yn = createString("delivery_yn");

    public final EnumPath<DeliveryInfo.Delivery_com> deliveryCom = createEnum("deliveryCom", DeliveryInfo.Delivery_com.class);

    public final NumberPath<Integer> deliveryPrice = createNumber("deliveryPrice", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QDeliveryInfo(String variable) {
        super(DeliveryInfo.class, forVariable(variable));
    }

    public QDeliveryInfo(Path<? extends DeliveryInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDeliveryInfo(PathMetadata metadata) {
        super(DeliveryInfo.class, metadata);
    }

}

