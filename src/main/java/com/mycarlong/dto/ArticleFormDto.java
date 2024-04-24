package com.mycarlong.dto;

import com.mycarlong.entity.Article;
import jakarta.validation.constraints.NotBlank;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;


public class ArticleFormDto {

	private Long id;
	@NotBlank(message = "제목은 비워둘 수 없습니다.")
	private String title;

	@NotBlank(message = "내용은 비워둘 수 없습니다.")
	private String content;

	@NotBlank(message = "글쓴이는 비워둘 수 없습니다.")
	private String author;

	private int hasReply;

	private List<ArticleImageDTO> articleImgDtoList = new ArrayList<>();
	private List<Long> articleImgIds = new ArrayList<>();

	public Article createArticle(){
		return modelMapper.map(this, Article.class);
	}

	private static ModelMapper modelMapper = new ModelMapper();
	public static ArticleFormDto of(Article article){
		return modelMapper.map(article,ArticleFormDto.class);
	}

}
