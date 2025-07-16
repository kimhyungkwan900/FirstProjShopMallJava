package com.example.shop_mall_back.admin.tracking.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTrackingInfo is a Querydsl query type for TrackingInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTrackingInfo extends EntityPathBase<TrackingInfo> {

    private static final long serialVersionUID = 1903501771L;

    public static final QTrackingInfo trackingInfo = new QTrackingInfo("trackingInfo");

    public final StringPath courierCode = createString("courierCode");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final StringPath trackingNumber = createString("trackingNumber");

    public QTrackingInfo(String variable) {
        super(TrackingInfo.class, forVariable(variable));
    }

    public QTrackingInfo(Path<? extends TrackingInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTrackingInfo(PathMetadata metadata) {
        super(TrackingInfo.class, metadata);
    }

}

