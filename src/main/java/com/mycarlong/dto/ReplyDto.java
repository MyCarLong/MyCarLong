package com.mycarlong.dto;

import com.mycarlong.entity.Article;
import com.mycarlong.entity.Reply;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

@Getter @Setter
@Builder
public class ReplyDto {

	private Long id;

	@Length(max = 30)
	@NotEmpty(message = "작성자는 필수 입력 값 입니다.")
	private String author;

	@Length(max = 200)
	@NotEmpty(message = "내용을 입력해주세요")
	private String content;

	private Long articleId;

	private static ModelMapper modelMapper = new ModelMapper();
	public Reply createReply(){
		return modelMapper.map(this, Reply.class);
	}


}
