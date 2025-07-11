package com.example.shop_mall_back.common.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberProfile is a Querydsl query type for MemberProfile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberProfile extends EntityPathBase<MemberProfile> {

    private static final long serialVersionUID = -252776746L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberProfile memberProfile = new QMemberProfile("memberProfile");

    public final EnumPath<com.example.shop_mall_back.common.constant.Age> age = createEnum("age", com.example.shop_mall_back.common.constant.Age.class);

    public final EnumPath<com.example.shop_mall_back.common.constant.Gender> gender = createEnum("gender", com.example.shop_mall_back.common.constant.Gender.class);

    public final EnumPath<com.example.shop_mall_back.common.constant.Grade> grade = createEnum("grade", com.example.shop_mall_back.common.constant.Grade.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isMembership = createBoolean("isMembership");

    public final QMember member;

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath profileImgUrl = createString("profileImgUrl");

    public final EnumPath<com.example.shop_mall_back.common.constant.Role> role = createEnum("role", com.example.shop_mall_back.common.constant.Role.class);

    public QMemberProfile(String variable) {
        this(MemberProfile.class, forVariable(variable), INITS);
    }

    public QMemberProfile(Path<? extends MemberProfile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberProfile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberProfile(PathMetadata metadata, PathInits inits) {
        this(MemberProfile.class, metadata, inits);
    }

    public QMemberProfile(Class<? extends MemberProfile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

