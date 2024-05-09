package com.mycarlong.dto;


import com.mycarlong.entity.ArticleImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ArticleImageDto {
	private Long id;

	private String imageOriginName;

	private String imageSavedName;

	private String imageSavedPath;

	private static ModelMapper modelMapper = new ModelMapper();

	private Long articleId;

	private MultipartFile imgFile;

	public static ArticleImageDto of(ArticleImage articleImg) {
		return modelMapper.map(articleImg, ArticleImageDto.class);
	}
}
