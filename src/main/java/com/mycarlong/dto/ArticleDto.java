package com.mycarlong.dto;

import com.mycarlong.entity.Article;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ArticleDto {

	private Long articleId;
	@NotBlank(message = "제목은 비워둘 수 없습니다.")
	private String title;

	@NotBlank(message = "내용은 비워둘 수 없습니다.")
	private String content;

	@NotBlank(message = "글쓴이는 비워둘 수 없습니다.")
	private String author;

	@NotBlank(message = "카테고리는 비워둘 수 없습니다.")
	private String category;

	private int hasReply;

	@Builder.Default
	private List<ArticleImageDto> articleImageList = new ArrayList<>();
	@Builder.Default
	private List<ReplyFormDto> articleReplyList = new ArrayList<>();

	private static ModelMapper modelMapper = new ModelMapper();
	public static ArticleDto of(Article article){

		return modelMapper.map(article, ArticleDto.class);
	}


}
