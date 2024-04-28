package com.mycarlong.service;

import com.mycarlong.dto.ArticleDto;
import com.mycarlong.dto.ReplyDto;

import java.util.List;

public interface ArticleService {
	List<ArticleDto> findAllArticle();

	ArticleDto viewArticleDetail(Long article_id);

	ArticleDto registArticle(ArticleDto articleDto);

	ArticleDto modifyArticle(ArticleDto articleDto);

	void deleteArticle();

	//Reply Part
	ReplyDto registReply(ReplyDto replyDto);

	List<ReplyDto> getAllReply();  //WHEN article detail view request, All reply read.

	ReplyDto modifyReply();

	void deleteReply();
}
