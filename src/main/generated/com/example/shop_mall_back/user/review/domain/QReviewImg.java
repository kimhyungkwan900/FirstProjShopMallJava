package com.example.shop_mall_back.user.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewImg is a Querydsl query type for ReviewImg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewImg extends EntityPathBase<ReviewImg> {

    private static final long serialVersionUID = -911531416L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewImg reviewImg = new QReviewImg("reviewImg");

    public final StringPath filePath = createString("filePath");

    public final NumberPath<Integer> fileSize = createNumber("fileSize", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.example.shop_mall_back.user.review.domain.enums.ImageType> imageType = createEnum("imageType", com.example.shop_mall_back.user.review.domain.enums.ImageType.class);

    public final QReview review;

    public QReviewImg(String variable) {
        this(ReviewImg.class, forVariable(variable), INITS);
    }

    public QReviewImg(Path<? extends ReviewImg> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewImg(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewImg(PathMetadata metadata, PathInits inits) {
        this(ReviewImg.class, metadata, inits);
    }

    public QReviewImg(Class<? extends ReviewImg> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReview(forProperty("review")) : null;
    }

}

