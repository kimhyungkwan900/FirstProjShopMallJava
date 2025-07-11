package com.example.shop_mall_back.user.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = -1379304613L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.shop_mall_back.common.domain.member.QMember member;

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final com.example.shop_mall_back.common.domain.QProduct product;

    public final StringPath reviewContent = createString("reviewContent");

    public final NumberPath<Integer> reviewScore = createNumber("reviewScore", Integer.class);

    public final EnumPath<com.example.shop_mall_back.user.review.domain.enums.ReviewStatus> reviewStatus = createEnum("reviewStatus", com.example.shop_mall_back.user.review.domain.enums.ReviewStatus.class);

    public final StringPath reviewSummation = createString("reviewSummation");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.shop_mall_back.common.domain.member.QMember(forProperty("member")) : null;
        this.product = inits.isInitialized("product") ? new com.example.shop_mall_back.common.domain.QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

