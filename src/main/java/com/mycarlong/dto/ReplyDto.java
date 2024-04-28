package com.mycarlong.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class ReplyDto {

	private Long id;

	@Length(max = 30)
	@NotEmpty(message = "작성자는 필수 입력 값 입니다.")
	private String author;

	@Length(max = 200)
	@NotEmpty(message = "내용을 입력해주세요")
	private String content;

	private Long articleId;
}
