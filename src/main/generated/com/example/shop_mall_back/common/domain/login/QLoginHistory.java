package com.example.shop_mall_back.common.domain.login;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLoginHistory is a Querydsl query type for LoginHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLoginHistory extends EntityPathBase<LoginHistory> {

    private static final long serialVersionUID = -429679669L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLoginHistory loginHistory = new QLoginHistory("loginHistory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath ipAddress = createString("ipAddress");

    public final EnumPath<com.example.shop_mall_back.common.constant.LoginResult> loginResult = createEnum("loginResult", com.example.shop_mall_back.common.constant.LoginResult.class);

    public final DateTimePath<java.time.LocalDateTime> loginTime = createDateTime("loginTime", java.time.LocalDateTime.class);

    public final EnumPath<com.example.shop_mall_back.common.constant.LoginType> loginType = createEnum("loginType", com.example.shop_mall_back.common.constant.LoginType.class);

    public final DateTimePath<java.time.LocalDateTime> logoutTime = createDateTime("logoutTime", java.time.LocalDateTime.class);

    public final com.example.shop_mall_back.common.domain.member.QMember member;

    public final StringPath userAgent = createString("userAgent");

    public QLoginHistory(String variable) {
        this(LoginHistory.class, forVariable(variable), INITS);
    }

    public QLoginHistory(Path<? extends LoginHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLoginHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLoginHistory(PathMetadata metadata, PathInits inits) {
        this(LoginHistory.class, metadata, inits);
    }

    public QLoginHistory(Class<? extends LoginHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.shop_mall_back.common.domain.member.QMember(forProperty("member")) : null;
    }

}

