package com.example.shop_mall_back.admin.banner.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBanner is a Querydsl query type for Banner
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBanner extends EntityPathBase<Banner> {

    private static final long serialVersionUID = -1228428825L;

    public static final QBanner banner = new QBanner("banner");

    public final StringPath alt = createString("alt");

    public final NumberPath<Integer> displayOrder = createNumber("displayOrder", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath link = createString("link");

    public final DateTimePath<java.time.LocalDateTime> visibleFrom = createDateTime("visibleFrom", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> visibleTo = createDateTime("visibleTo", java.time.LocalDateTime.class);

    public QBanner(String variable) {
        super(Banner.class, forVariable(variable));
    }

    public QBanner(Path<? extends Banner> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBanner(PathMetadata metadata) {
        super(Banner.class, metadata);
    }

}

