package com.mycarlong.service;

import com.mycarlong.dto.ReplyDto;

import java.util.List;

public interface ReplyService {

	//Reply Part
	ReplyDto registReply(ReplyDto replyDto);

	List<ReplyDto> getAllReply();  //WHEN article detail view request, All reply read.

	ReplyDto modifyReply();

	void deleteReply();
}