package com.mycarlong.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArticle is a Querydsl query type for Article
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArticle extends EntityPathBase<Article> {

    private static final long serialVersionUID = 856232866L;

    public static final QArticle article = new QArticle("article");

    public final StringPath author = createString("author");

    public final StringPath category = createString("category");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> hasImgaes = createNumber("hasImgaes", Integer.class);

    public final BooleanPath hasImgaesFlag = createBoolean("hasImgaesFlag");

    public final BooleanPath hasRelplyFlag = createBoolean("hasRelplyFlag");

    public final NumberPath<Integer> hasReply = createNumber("hasReply", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<ArticleImage, QArticleImage> thisImgList = this.<ArticleImage, QArticleImage>createList("thisImgList", ArticleImage.class, QArticleImage.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public QArticle(String variable) {
        super(Article.class, forVariable(variable));
    }

    public QArticle(Path<? extends Article> path) {
        super(path.getType(), path.getMetadata());
    }

    public QArticle(PathMetadata metadata) {
        super(Article.class, metadata);
    }

}

