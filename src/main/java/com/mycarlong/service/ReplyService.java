package com.mycarlong.service;

import com.mycarlong.dto.ReplyDto;

import java.util.List;

public interface ReplyService {
	void addReply(ReplyDto replyDto);

	List<ReplyDto> getAllReply();

	void modify(ReplyDto replyDto);

	void delete(Long reply_id);
}
