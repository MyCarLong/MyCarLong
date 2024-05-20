package com.mycarlong.service;

import com.mycarlong.dto.ReplyFormDto;

import java.util.List;

public interface ReplyService {

	//Reply Part
	void registReply(ReplyFormDto replyFormDto);

	List<ReplyFormDto> getAllReply(Long articleId);//WHEN article detail view request, All reply read.

	void modifyReply(Long articleId, ReplyFormDto replyFormDto);

	void deleteReply(Long articleId,Long replyId);
}