package com.mycarlong.service;

import com.mycarlong.dto.ReplyDto;
import com.mycarlong.entity.Article;
import com.mycarlong.entity.Reply;
import com.mycarlong.repository.ArticleRepository;
import com.mycarlong.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{
	private final ArticleRepository articleRepository;
	private final ReplyRepository replyRepository;
	@Override
	public void registReply(ReplyDto replyDto) {
		Reply thisReply = replyDto.createReply();
		Article article = articleRepository.findById(replyDto.getArticleId()).orElse(null);
		thisReply.setArticle(article);
		replyRepository.save(thisReply);
	}

	@Override
	public List<ReplyDto> getAllReply(Long articleId) {
		return replyRepository.findAllReplyByArticleId(articleId).stream().map(ReplyDto::of).collect(Collectors.toList());
	}

	@Override
	public void modifyReply() {

	}

	@Override
	public void deleteReply() {

	}
}
