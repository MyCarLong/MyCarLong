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


@Service
@Transactional
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{
	private final ArticleRepository articleRepository;
	private final ReplyRepository replyRepository;
	@Override
	public ReplyDto registReply(ReplyDto replyDto) {
		Reply thisReply = replyDto.createReply();
		Article article = articleRepository.findById(replyDto.getArticleId()).orElse(null);
		thisReply.setArticle(article);
		replyRepository.save(thisReply);

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
