package com.mycarlong.service;

import com.mycarlong.dto.ArticleDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArticleService {
	List<ArticleDto> findAllArticle();
	List<ArticleDto> findFiftyArticldOrderByDesc();
	ArticleDto viewArticleDetail(Long article_id);

	void registArticle(ArticleDto articleDto, List<MultipartFile> multipartFileList) throws IOException;

	void modifyArticle(Long articleId, ArticleDto articleDto);

	void deleteArticle(Long articleId, ArticleDto articleDto);

}
