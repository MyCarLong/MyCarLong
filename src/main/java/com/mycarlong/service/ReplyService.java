package com.mycarlong.service;

import com.mycarlong.dto.ReplyDto;

import java.util.List;

public interface ReplyService {

	//Reply Part
	void registReply(ReplyDto replyDto);



	List<ReplyDto> getAllReply(Long articleId);//WHEN article detail view request, All reply read.

	void modifyReply();

	void deleteReply();
}