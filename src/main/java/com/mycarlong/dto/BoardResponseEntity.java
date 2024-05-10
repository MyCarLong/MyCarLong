package com.mycarlong.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;


@Getter @Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class BoardResponseEntity {
	private List<ArticleDto> articleList;
	private Map<String, String> errorDetails;
	private ArticleDto article;
	private List<ReplyDto> replyDtoList;
	private HttpStatus status;
}
