package com.mycarlong.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QKakaoUser is a Querydsl query type for KakaoUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKakaoUser extends EntityPathBase<KakaoUser> {

    private static final long serialVersionUID = -1867834662L;

    public static final QKakaoUser kakaoUser = new QKakaoUser("kakaoUser");

    public final StringPath name = createString("name");

    public final StringPath pwd = createString("pwd");

    public final StringPath userid = createString("userid");

    public QKakaoUser(String variable) {
        super(KakaoUser.class, forVariable(variable));
    }

    public QKakaoUser(Path<? extends KakaoUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKakaoUser(PathMetadata metadata) {
        super(KakaoUser.class, metadata);
    }

}

