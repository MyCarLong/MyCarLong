package com.mycarlong.service;

import com.mycarlong.dto.ArticleDto;

import java.util.List;

public interface ArticleService {
	List<ArticleDto> findAllArticle();

	ArticleDto viewArticleDetail(Long article_id);

	void registArticle(ArticleDto articleDto);

	void modifyArticle(ArticleDto articleDto);

	void deleteArticle();

}
