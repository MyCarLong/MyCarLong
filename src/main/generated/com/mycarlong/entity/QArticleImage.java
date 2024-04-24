package com.mycarlong.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArticleImage is a Querydsl query type for ArticleImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArticleImage extends EntityPathBase<ArticleImage> {

    private static final long serialVersionUID = -816604455L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArticleImage articleImage = new QArticleImage("articleImage");

    public final QArticle articleId;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageOriginName = createString("imageOriginName");

    public final StringPath imageSavedName = createString("imageSavedName");

    public final StringPath imageSavedPath = createString("imageSavedPath");

    public QArticleImage(String variable) {
        this(ArticleImage.class, forVariable(variable), INITS);
    }

    public QArticleImage(Path<? extends ArticleImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArticleImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArticleImage(PathMetadata metadata, PathInits inits) {
        this(ArticleImage.class, metadata, inits);
    }

    public QArticleImage(Class<? extends ArticleImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.articleId = inits.isInitialized("articleId") ? new QArticle(forProperty("articleId")) : null;
    }

}

