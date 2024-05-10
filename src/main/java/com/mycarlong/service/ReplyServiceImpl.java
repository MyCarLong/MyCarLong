package com.mycarlong.service;

import com.mycarlong.dto.ReplyDto;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReplyServiceImpl implements ReplyService{
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
