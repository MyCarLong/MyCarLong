package com.mycarlong.service;

import com.mycarlong.dto.ArticleDto;
import com.mycarlong.dto.ReplyDto;
import com.mycarlong.repository.ArticleImageRepository;
import com.mycarlong.repository.ArticleRepository;
import com.mycarlong.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		return List.of();
	}

	@Override
	public ArticleDto viewArticleDetail(Long article_id) {
		return null;
	}

	@Override
	public ArticleDto registArticle(ArticleDto articleDto) {
		return null;
	}

	@Override
	public ArticleDto modifyArticle(ArticleDto articleDto) {
		return null;
	}

	@Override
	public void deleteArticle() {

	}

	@Override
	public ReplyDto registReply(ReplyDto replyDto) {
		return null;
	}

	@Override
	public List<ReplyDto> getAllReply() {
		return List.of();
	}

	@Override
	public ReplyDto modifyReply() {
		return null;
	}

	@Override
	public void deleteReply() {

	}
}
