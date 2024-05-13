package com.mycarlong.service;

import com.mycarlong.dto.ArticleDto;
import com.mycarlong.dto.ArticleFormDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArticleService {
	List<ArticleDto> findAllArticle();

	ArticleDto viewArticleDetail(Long article_id);

	List<ArticleDto> findFiftyArticldOrderByDesc();

	void registArticle(ArticleFormDto articleDto, List<MultipartFile> multipartFileList) throws IOException;

	void modifyArticle(Long articleId, ArticleFormDto articleDto);

	void deleteArticle(Long articleId, ArticleFormDto articleDto);

	List<ArticleDto> findByCategory(String category);
}
