package com.mycarlong.service;

import com.mycarlong.dto.ArticleDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArticleService {
	List<ArticleDto> findAllArticle();

	ArticleDto viewArticleDetail(Long article_id);

	List<ArticleDto> findFiftyArticldOrderByDesc();

	void registArticle(ArticleDto articleDto, List<MultipartFile> multipartFileList) throws IOException;

	void modifyArticle(Long articleId, ArticleDto articleDto);

	void deleteArticle(Long articleId, ArticleDto articleDto);

	List<ArticleDto> findByCategory(String category);
}
