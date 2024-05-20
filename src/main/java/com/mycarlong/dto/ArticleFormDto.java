package com.mycarlong.dto;

import com.mycarlong.entity.Article;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ArticleFormDto {

	@NotBlank(message = "제목은 비워둘 수 없습니다.")
	private String title;

	@NotBlank(message = "내용은 비워둘 수 없습니다.")
	private String content;

	@NotBlank(message = "글쓴이는 비워둘 수 없습니다.")
	private String author;

	@NotBlank(message = "글쓴이는 비워둘 수 없습니다.")
	private String category;

	@Builder.Default
	private List<MultipartFile> articleImgList = new ArrayList<>();

	public Article createArticle(){
		return modelMapper.map(this, Article.class);
	}

	private static ModelMapper modelMapper = new ModelMapper();
	public static ArticleDto of(Article article){
		return modelMapper.map(article, ArticleDto.class);
	}
}
