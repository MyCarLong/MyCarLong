package com.mycarlong.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Getter @Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class BoardResponseEntity {
	private HttpStatus status;
	private List<ArticleDto> articleList;
	private ArticleDto article;
//	@Builder.Default
	private List<ReplyFormDto> replyFormDtoList;
	private Map<String, String> errorDetails;

}