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
	@Builder.Default
	private List<ArticleDto> articleList = new ArrayList<>();
	private Map<String, String> errorDetails;
	private ArticleDto article;
	@Builder.Default
	private List<ReplyDto> replyDtoList = new ArrayList<>();
	private HttpStatus status;
}
