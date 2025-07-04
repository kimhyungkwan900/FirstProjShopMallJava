package com.example.shop_mall_back.admin.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReviewBlind is a Querydsl query type for ReviewBlind
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewBlind extends EntityPathBase<ReviewBlind> {

    private static final long serialVersionUID = -1954983594L;

    public static final QReviewBlind reviewBlind = new QReviewBlind("reviewBlind");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> blindAt = createDateTime("blindAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath reason = createString("reason");

    public final NumberPath<Long> reviewId = createNumber("reviewId", Long.class);

    public QReviewBlind(String variable) {
        super(ReviewBlind.class, forVariable(variable));
    }

    public QReviewBlind(Path<? extends ReviewBlind> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReviewBlind(PathMetadata metadata) {
        super(ReviewBlind.class, metadata);
    }

}

