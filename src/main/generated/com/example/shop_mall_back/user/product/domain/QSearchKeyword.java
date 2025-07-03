package com.example.shop_mall_back.user.product.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSearchKeyword is a Querydsl query type for SearchKeyword
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSearchKeyword extends EntityPathBase<SearchKeyword> {

    private static final long serialVersionUID = -760464507L;

    public static final QSearchKeyword searchKeyword = new QSearchKeyword("searchKeyword");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath keyword = createString("keyword");

    public QSearchKeyword(String variable) {
        super(SearchKeyword.class, forVariable(variable));
    }

    public QSearchKeyword(Path<? extends SearchKeyword> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSearchKeyword(PathMetadata metadata) {
        super(SearchKeyword.class, metadata);
    }

}

