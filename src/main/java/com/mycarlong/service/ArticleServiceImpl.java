package com.mycarlong.service;

import com.mycarlong.dto.ArticleDto;
import com.mycarlong.entity.Article;
import com.mycarlong.repository.ArticleImageRepository;
import com.mycarlong.repository.ArticleRepository;
import com.mycarlong.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

	private final ArticleRepository articleRepository;
	private final ArticleImageServiceImpl articleImageService;
	private final ArticleImageRepository articleImageRepository;
	private final ReplyRepository replyRepository;

//	public ArticleServiceImpl(ArticleRepository articleRepository, ArticleImageRepository articleImageRepository, ArticleImageServiceImpl articleImageService, FileRepository fileRepository, ReplyRepository replyRepository) {
//		this.articleRepository = articleRepository;
//		this.articleImageService = articleImageService;
//		this.fileRepository = fileRepository;
//		this.replyRepository = replyRepository;
//	}

	@Override
	public List<ArticleDto> findAllArticle() {
		List<Article> articleList = articleRepository.findAll();
		List<ArticleDto> articleDtoList = new ArrayList<>();

		for( Article article: articleList ){
			ArticleDto articleDto = ArticleDto.builder()
					.id(article.getId())
					.title(article.getTitle())
					.content(article.getContent())
					.author(article.getAuthor())
					.category(article.getCategory())
					.articleImgList(article.getThisImgList())
					.replyList(article.getThisReplyList())
					.build();

			articleDtoList.add(articleDto);
		}
		return articleDtoList;
	}

	@Override
	@Transactional
	public void registArticle(ArticleDto articleDto) {
		Article article = articleDto.createArticle();
		articleRepository.save(article);
	}

	@Override
	public ArticleDto viewArticleDetail(Long article_id) {
		return null;
	}



	@Override
	public void modifyArticle(ArticleDto articleDto) {
		Article article = articleDto.createArticle();
		if(!verifyUser(article)) {

		}
	}

	@Override
	public void deleteArticle() {

	}
	/*
	* 요청자가 author인 경우에만 게시글의 modify , delete를 가능케 한다. <br>
	*
	* 요청자를 확인하는 함수 verifyUser()
	* */
	private boolean verifyUser(Article article) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		if(article.getAuthor().equals(username)){
			return true;
		}else return false;
	}
}
