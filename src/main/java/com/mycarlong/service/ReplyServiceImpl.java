package com.mycarlong.service;

import com.mycarlong.dto.ReplyFormDto;
import com.mycarlong.entity.Article;
import com.mycarlong.entity.Reply;
import com.mycarlong.exception.AuthorizationException;
import com.mycarlong.exception.DatabaseAccessException;
import com.mycarlong.repository.ArticleRepository;
import com.mycarlong.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
	public void registReply(ReplyFormDto replyFormDto) {
		Reply thisReply = replyFormDto.createReply();
		Article article = articleRepository.findById(replyFormDto.getArticleId()).orElse(null);
		thisReply.setArticle(article);
		replyRepository.save(thisReply);
	}

	@Override
	public List<ReplyFormDto> getAllReply(Long articleId) {
		return replyRepository.findAllReplyByArticleId(articleId).stream().map(ReplyFormDto::of).collect(Collectors.toList());
	}

	@Override
	public void modifyReply(Long articleId, ReplyFormDto replyFormDto) {
		verifyUser(replyFormDto);
		Reply thisInputReply = replyFormDto.createReply();
		Reply deleteTarget = replyRepository.findById(replyFormDto.getId()).orElseThrow(() ->
				new DatabaseAccessException("Database에 일치하는 데이터가 없습니다.",null));
		replyRepository.delete(deleteTarget);
		replyRepository.save(thisInputReply);
	}

	@Override
	public void deleteReply(Long articleId, Long replyId) {
		Reply reply = replyRepository.findById(replyId).orElseThrow(() ->
				new DatabaseAccessException("Database에 일치하는 데이터가 없습니다.",null));
		ReplyFormDto replyFormDto = ReplyFormDto.of(reply);
		verifyUser(replyFormDto);  // 내부 메서드에서 로그인 사용자를 판별하고 사용자와 게시자가 다르면 예외를 던진다.
		replyRepository.deleteById(replyId);
	}

	private void verifyUser(ReplyFormDto replyFormDto) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			if (!username.equals(replyFormDto.getAuthor())) {
				throw new AuthorizationException("댓글에 대한 권한이 없는 유저입니다...", null);
			}
		}
	}
}
